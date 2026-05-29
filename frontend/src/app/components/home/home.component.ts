import { Component, OnInit, OnDestroy, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterModule } from '@angular/router'; 
import { FormsModule } from '@angular/forms';
import { Subscription } from 'rxjs';
import { AuthService } from '../../services/auth.service';
import { ProyectoService } from '../../services/propiedad';
import { AgendaService } from '../../services/agenda.service';
import { HostListener } from '@angular/core';
import Swal from 'sweetalert2';

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
    if (!target.closest('.btn-puntos')) {
      this.menuAbiertoId = null;
    }
  }

  // --- Gestión de Proyectos ---
  proyectos: any[] = [];
  menuAbiertoId: number | null = null; 
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
    this.idUsuario = Number(localStorage.getItem('id_usuario'));
    this.nombreUsuario = localStorage.getItem('nombre') || 'Usuario';
    this.rolUsuario = localStorage.getItem('rol')?.toUpperCase() || 'SIN ROL';

    this.refreshSub = this.proyectoService.refreshNeeded$.subscribe(() => {
      this.refrescarTodo();
    });
    this.refrescarTodo();
  }

  ngOnDestroy(): void {
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
            this.refrescarTodo(); 
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
          // MAPEAMOS ABSOLUTAMENTE TODO
          id: p.id_proyecto, 
          nombre: p.nombre,
          descripcion: p.descripcion,
          progreso: p.progreso || 0,
          estado: p.estado || 'Activo',
          // ESTAS SON LAS LÍNEAS QUE FALTABAN:
          latitud: p.latitud,
          longitud: p.longitud
        }));
        
        console.log('Proyectos mapeados correctamente:', this.proyectos);
        this.cd.detectChanges();
      },
      error: (err) => console.error('Error al cargar proyectos:', err)
    });
  }

  cargarAgendaReal(): void {
    this.agendaService.listarEventos().subscribe({ 
      next: (eventos) => {
        this.agendaEventos = {};
        eventos.forEach(ev => {
          this.agendaEventos[ev.dia] = { 
            id: ev.id, 
            titulo: ev.titulo 
          };
        });
        console.log('Agenda cargada con éxito (Modo Global)');
        this.cd.detectChanges();
      },
      error: (err) => console.error('Error al cargar agenda global:', err)
    });
  }

  // --- Acciones de Proyectos (Menú de tres puntos) ---

  toggleMenu(id: number): void {
    this.menuAbiertoId = this.menuAbiertoId === id ? null : id;
  }

  abrirEdicion(proyecto: any): void {
    this.menuAbiertoId = null; 
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
    console.log('Enviando actualización para ID:', proyecto.id);
    this.proyectoService.actualizarProgreso(proyecto.id, progresoNum).subscribe({
      next: () => console.log(`✅ Progreso de ${proyecto.nombre} guardado: ${progresoNum}%`),
      error: (err) => console.error('❌ Error al guardar progreso:', err)
    });
  }

  // --- Lógica de la Agenda ---

  abrirModal(dia: number) {
    this.diaSeleccionado = dia;
    this.nuevoEventoTexto = this.agendaEventos[dia]?.titulo || '';
    this.mostrarModal = true;
  }

  guardarEvento() {
    if (this.diaSeleccionado !== null) {
      const textoTrim = this.nuevoEventoTexto.trim();
      if (textoTrim !== '') {
        const eventoExistente = this.agendaEventos[this.diaSeleccionado];
        const eventoRequest: any = {
          titulo: textoTrim,
          dia: this.diaSeleccionado,
          mesAnio: this.mesActual,
          idUsuario: this.idUsuario 
        };
        if (eventoExistente && eventoExistente.id) {
          eventoRequest.id = eventoExistente.id;
        }
        console.log('Enviando a la agenda global:', eventoRequest);
        this.agendaService.guardar(eventoRequest).subscribe({
          next: () => {
            this.cerrarModal();
            this.refrescarTodo(); 
          },
          error: (err) => {
            console.error('Error al procesar evento:', err);
            alert('No se pudo guardar el evento. Revisa la consola.');
          }
        });
      }
    }
  }

  cerrarModal() {
    this.mostrarModal = false;
    this.diaSeleccionado = null;
    this.nuevoEventoTexto = '';
  }

  verUbicacion(lat: number, lng: number): void {
    console.log("Latitud:", lat, "Longitud:", lng);
    if (lat != null && lng != null) {
      // URL corregida para Google Maps
      const url = `https://www.google.com/maps?q=${lat},${lng}`;
      window.open(url, '_blank');
    } else {
      Swal.fire({
        title: 'Sin coordenadas',
        text: 'Este proyecto no tiene ubicación guardada.',
        icon: 'info',
        background: '#111',
        color: '#fff'
      });
    }
  }

  // --- Sesión ---

  logout(): void {
    this.authService.logout();
    this.router.navigate(['/login']);
  }
  irAUsuarios() {
    this.router.navigate(['/usuarios']);
  }
  get isAdmin(): boolean {
    return this.authService.isAdmin(); // Usando el método que creamos
  }
  
  get isPropietario(): boolean {
    return this.authService.isPropietario();
  }
  get canEditProyectos(): boolean {
    return this.authService.isAdmin(); // Solo admin puede editar/borrar
  }
}