import { Component, OnInit, OnDestroy, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterModule } from '@angular/router'; 
import { FormsModule } from '@angular/forms';
import { Subscription } from 'rxjs';
import { AuthService } from '../../services/auth.service';
import { ProyectoService } from '../../services/propiedad';
import { AgendaService } from '../../services/agenda.service';
import { HostListener } from '@angular/core';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [CommonModule, RouterModule, FormsModule],
  templateUrl: './home.component.html'
})
export class HomeComponent implements OnInit, OnDestroy {
  // --- Datos de Usuario ---
  nombreUsuario: string = '';
  rolUsuario: string = '';
  idUsuario: number | null = null;

  @HostListener('document:click', ['$event'])
  onDocumentClick(event: MouseEvent) {
    const target = event.target as HTMLElement;
    // Si el clic no fue en el botón de los puntos, cerramos el menú
    if (!target.closest('.btn-puntos')) {
      this.menuAbiertoId = null;
    }
  }

  // --- Gestión de Proyectos ---
  proyectos: any[] = [];
  menuAbiertoId: number | null = null; // Controla el menú de tres puntos
  private refreshSub!: Subscription;

  // --- Lógica del Calendario / Agenda ---
  mesActual: string = 'Mayo 2026';
  diasMes: number[] = Array.from({ length: 31 }, (_, i) => i + 1);
  agendaEventos: { [key: number]: any } = {}; 

  // --- Estado de Modales ---
  mostrarModal: boolean = false;
  diaSeleccionado: number | null = null;
  nuevoEventoTexto: string = '';

  constructor(
    private authService: AuthService, 
    private router: Router,
    private proyectoService: ProyectoService,
    private agendaService: AgendaService,
    private cd: ChangeDetectorRef 
  ) {}

  ngOnInit(): void {
    // Recuperar información de sesión
    this.idUsuario = Number(localStorage.getItem('id_usuario'));
    this.nombreUsuario = localStorage.getItem('nombre') || 'Usuario';
    this.rolUsuario = localStorage.getItem('rol')?.toUpperCase() || 'SIN ROL';

    // Suscripción reactiva para refrescar datos automáticamente
    this.refreshSub = this.proyectoService.refreshNeeded$.subscribe(() => {
      this.refrescarTodo();
    });
    this.refrescarTodo();
  }

  ngOnDestroy(): void {
    // Limpieza de suscripción para evitar fugas de memoria
    if (this.refreshSub) this.refreshSub.unsubscribe();
  }

  // --- Métodos de Carga de Datos ---

  refrescarTodo(): void {
    this.cargarProyectosReales();
    this.cargarAgendaReal();
  }
  borrarEvento(): void {
    const evento = this.agendaEventos[this.diaSeleccionado!];
  
    if (evento && evento.id) {
      if (confirm(`¿Estás seguro de que deseas borrar la agenda del día ${this.diaSeleccionado}?`)) {
        this.agendaService.eliminar(evento.id).subscribe({
          next: () => {
            console.log('✅ Evento eliminado correctamente');
            this.cerrarModal();
            this.refrescarTodo(); // Para que el número en el calendario se limpie
          },
          error: (err) => {
            console.error('❌ Error al eliminar el evento:', err);
            alert('No se pudo eliminar el evento de la base de datos.');
          }
        });
      }
    }
  }

  cargarProyectosReales(): void {
    this.proyectoService.listarProyectos().subscribe({
      next: (data) => {
        this.proyectos = data.map((p: any) => ({
          // CAMBIO AQUÍ: Usamos id_proyecto para que coincida con tu base de datos
          id: p.id_proyecto, 
          nombre: p.nombre,
          descripcion: p.descripcion,
          progreso: p.progreso || 0,
          estado: p.estado || 'Activo'
        }));
        
        console.log('Proyectos mapeados correctamente:', this.proyectos);
        this.cd.detectChanges();
      },
      error: (err) => console.error('Error al cargar proyectos:', err)
    });
  }

  cargarAgendaReal(): void {
    if (this.idUsuario) {
      this.agendaService.listarPorUsuario(this.idUsuario).subscribe({
        next: (eventos) => {
          this.agendaEventos = {};
          eventos.forEach(ev => {
            this.agendaEventos[ev.dia] = { id: ev.id, titulo: ev.titulo };
          });
          this.cd.detectChanges();
        },
        error: (err) => console.error('Error al cargar agenda:', err)
      });
    }
  }

  // --- Acciones de Proyectos (Menú de tres puntos) ---

  toggleMenu(id: number): void {
    // Si el id es el mismo que ya está abierto, lo cerramos (null)
    // Si es uno diferente, abrimos ese y se cierra el anterior automáticamente
    this.menuAbiertoId = this.menuAbiertoId === id ? null : id;
  }

  abrirEdicion(proyecto: any): void {
    this.menuAbiertoId = null; // Cerramos el menú de tres puntos
    
    // Redirigimos a la ruta de crear, pero enviando el ID por parámetro
    // Asegúrate de que 'id' sea el correcto (id_proyecto)
    this.router.navigate(['/crear-propiedad'], { queryParams: { id: proyecto.id } });
  }

  confirmarEliminar(id: number, nombre: string): void {
    if (confirm(`¿Estás seguro de que deseas eliminar el proyecto "${nombre}"?`)) {
      this.proyectoService.eliminarProyecto(id).subscribe({
        next: () => {
          console.log('Proyecto eliminado');
          this.menuAbiertoId = null;
          this.refrescarTodo();
        },
        error: (err) => {
          console.error('Error al eliminar:', err);
          alert('No se pudo eliminar el proyecto. Verifica permisos.');
        }
      });
    }
  }

  actualizarProgresoProyecto(proyecto: any, nuevoValor: any): void {
    const progresoNum = Number(nuevoValor);
    proyecto.progreso = progresoNum;
  
    // Verificamos en consola si el ID existe antes de enviar
    console.log('Enviando actualización para ID:', proyecto.id);
  
    this.proyectoService.actualizarProgreso(proyecto.id, progresoNum).subscribe({
      next: () => {
        console.log(`✅ Progreso de ${proyecto.nombre} guardado: ${progresoNum}%`);
      },
      error: (err) => {
        console.error('❌ Error al guardar progreso:', err);
      }
    });
  }

  // --- Lógica de la Agenda ---

  abrirModal(dia: number) {
    this.diaSeleccionado = dia;
    this.nuevoEventoTexto = this.agendaEventos[dia]?.titulo || '';
    this.mostrarModal = true;
  }

  guardarEvento() {
    if (this.diaSeleccionado !== null && this.idUsuario) {
      const textoTrim = this.nuevoEventoTexto.trim();
      if (textoTrim !== '') {
        
        // 1. Buscamos si ya existe un evento para este día en nuestro objeto local
        const eventoExistente = this.agendaEventos[this.diaSeleccionado];
  
        const eventoRequest = {
          // Si existe, le pasamos su ID; si no, queda null y Spring Boot crea uno nuevo
          id: eventoExistente ? eventoExistente.id : null, 
          titulo: textoTrim,
          dia: this.diaSeleccionado,
          mesAnio: this.mesActual,
          idUsuario: this.idUsuario
        };
  
        console.log('Enviando a la agenda:', eventoRequest);
  
        this.agendaService.guardar(eventoRequest).subscribe({
          next: () => {
            this.cerrarModal();
            this.refrescarTodo();
          },
          error: (err) => console.error('Error al procesar evento:', err)
        });
      }
    }
  }

  cerrarModal() {
    this.mostrarModal = false;
    this.diaSeleccionado = null;
    this.nuevoEventoTexto = '';
  }

  // --- Sesión ---

  logout(): void {
    this.authService.logout();
    this.router.navigate(['/login']);
  }
}