import { Component, OnInit } from '@angular/core'; // Añadimos OnInit
import { Router, RouterModule, ActivatedRoute } from '@angular/router'; // Añadimos ActivatedRoute
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { ProyectoService } from '../../services/propiedad'; 
import Swal from 'sweetalert2';

@Component({
  selector: 'app-crear-propiedad',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule], 
  templateUrl: './crear-propiedad.html',
  styleUrls: ['./crear-propiedad.css']
})
export class CrearPropiedad implements OnInit {

  proyecto: any = {
    nombre: '',
    fecha_inicio: '',
    fecha_fin: '',
    descripcion: '',
    estado: 'ACTIVO',
    progreso: 0
  };

  esEdicion: boolean = false;
  idProyectoEdicion: number | null = null;

  constructor(
    private proyectoService: ProyectoService,
    private router: Router,
    private route: ActivatedRoute // Para detectar el ID en la URL
  ) {}

  ngOnInit(): void {
    // Verificamos si viene un ID por parámetro (ej: /crear-propiedad?id=5)
    const idParam = this.route.snapshot.queryParamMap.get('id');
    
    if (idParam) {
      this.esEdicion = true;
      this.idProyectoEdicion = Number(idParam);
      this.cargarDatosParaEditar(this.idProyectoEdicion);
    }
  }

  cargarDatosParaEditar(id: number): void {
    // Nota: Necesitas el método obtenerProyectoPorId en tu servicio
    this.proyectoService.obtenerProyectoPorId(id).subscribe({
      next: (data) => {
        // Rellenamos el objeto proyecto con lo que viene de la BD
        this.proyecto = {
          nombre: data.nombre,
          fecha_inicio: data.fecha_inicio,
          fecha_fin: data.fecha_fin,
          descripcion: data.descripcion,
          estado: data.estado,
          progreso: data.progreso
        };
      },
      error: (err) => {
        console.error('Error al cargar datos:', err);
        Swal.fire('Error', 'No se pudieron cargar los datos del proyecto', 'error');
      }
    });
  }

  guardar(): void {
    // 1. VALIDACIÓN DE LÓGICA DE NEGOCIO
    if (this.proyecto.fecha_inicio && this.proyecto.fecha_fin) {
      const inicio = new Date(this.proyecto.fecha_inicio);
      const fin = new Date(this.proyecto.fecha_fin);
  
      if (fin < inicio) {
        Swal.fire({
          title: 'Cronograma Inválido',
          text: 'La fecha de finalización no puede ser anterior a la fecha de inicio.',
          icon: 'warning',
          confirmButtonColor: '#2ecc71',
          background: '#111',
          color: '#fff'
        });
        return; // CORTA LA EJECUCIÓN: No permite que el código siga al servicio
      }
    }
  
    // 2. PROCESAMIENTO DE ENVÍO
    if (this.esEdicion && this.idProyectoEdicion) {
      // Lógica de ACTUALIZAR
      this.proyectoService.actualizarProyecto(this.idProyectoEdicion, this.proyecto).subscribe({
        next: () => this.mostrarExito('Propiedad actualizada con éxito.'),
        error: (err) => this.mostrarError(err)
      });
    } else {
      // Lógica de CREAR
      this.proyectoService.crearProyecto(this.proyecto).subscribe({
        next: () => this.mostrarExito('Propiedad registrada en Fusagasugá.'),
        error: (err) => this.mostrarError(err)
      });
    }
  }

  // Métodos auxiliares para no repetir código de alertas
  private mostrarExito(mensaje: string): void {
    Swal.fire({
      title: '¡Éxito!',
      text: mensaje,
      icon: 'success',
      confirmButtonColor: '#2ecc71',
      background: '#111',
      color: '#fff'
    }).then(() => this.router.navigate(['/home']));
  }

  private mostrarError(err: any): void {
    console.error('Error en la operación:', err);
    Swal.fire({
      title: 'Error',
      text: 'No se pudo completar la operación.',
      icon: 'error',
      background: '#111',
      color: '#fff'
    });
  }
}