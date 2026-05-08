import { Component, OnInit, AfterViewInit } from '@angular/core'; // Añadimos AfterViewInit
import { Router, RouterModule, ActivatedRoute } from '@angular/router'; 
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { ProyectoService } from '../../services/propiedad'; 
import Swal from 'sweetalert2';
import * as L from 'leaflet'; // Importamos Leaflet

@Component({
  selector: 'app-crear-propiedad',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule], 
  templateUrl: './crear-propiedad.html',
  styleUrls: ['./crear-propiedad.css']
})
export class CrearPropiedad implements OnInit, AfterViewInit {

  proyecto: any = {
    nombre: '',
    fecha_inicio: '',
    fecha_fin: '',
    descripcion: '',
    estado: 'ACTIVO',
    progreso: 0,
    latitud: 4.33646, // Coordenadas por defecto (Fusa)
    longitud: -74.36378
  };

  esEdicion: boolean = false;
  idProyectoEdicion: number | null = null;

  // Variables para el mapa
  private map: any;
  private marker: any;

  constructor(
    private proyectoService: ProyectoService,
    private router: Router,
    private route: ActivatedRoute 
  ) {}

  ngOnInit(): void {
    const idParam = this.route.snapshot.queryParamMap.get('id');
    
    if (idParam) {
      this.esEdicion = true;
      this.idProyectoEdicion = Number(idParam);
      this.cargarDatosParaEditar(this.idProyectoEdicion);
    }
  }

  // Inicializamos el mapa después de que la vista cargue
  ngAfterViewInit(): void {
    this.initMap();
  }

  private initMap(): void {
    // Usar coordenadas del proyecto o las de Fusa por defecto
    const lat = this.proyecto.latitud || 4.33646;
    const lng = this.proyecto.longitud || -74.36378;

    this.map = L.map('map', {
      center: [lat, lng],
      zoom: 15
    });

    L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
      attribution: '&copy; OpenStreetMap contributors'
    }).addTo(this.map);

    this.marker = L.marker([lat, lng], {
      draggable: true
    }).addTo(this.map);

    // Actualizar coordenadas al mover marcador
    this.marker.on('dragend', () => {
      const position = this.marker.getLatLng();
      this.proyecto.latitud = position.lat;
      this.proyecto.longitud = position.lng;
    });

    // Mover marcador al hacer clic en el mapa
    this.map.on('click', (e: any) => {
      this.marker.setLatLng(e.latlng);
      this.proyecto.latitud = e.latlng.lat;
      this.proyecto.longitud = e.latlng.lng;
    });

    // Ajuste de tamaño para evitar errores de renderizado
    setTimeout(() => {
      this.map.invalidateSize();
    }, 200);
  }

  cargarDatosParaEditar(id: number): void {
    this.proyectoService.obtenerProyectoPorId(id).subscribe({
      next: (data) => {
        this.proyecto = {
          nombre: data.nombre,
          fecha_inicio: data.fecha_inicio,
          fecha_fin: data.fecha_fin,
          descripcion: data.descripcion,
          estado: data.estado,
          progreso: data.progreso,
          latitud: data.latitud, // Cargamos latitud de la BD
          longitud: data.longitud // Cargamos longitud de la BD
        };

        // Si el mapa ya existe, movemos el marcador a la posición guardada
        if (this.map && this.proyecto.latitud) {
          const coords = new L.LatLng(this.proyecto.latitud, this.proyecto.longitud);
          this.map.setView(coords, 15);
          this.marker.setLatLng(coords);
        }
      },
      error: (err) => {
        console.error('Error al cargar datos:', err);
        Swal.fire('Error', 'No se pudieron cargar los datos del proyecto', 'error');
      }
    });
  }

  guardar(): void {
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
        return; 
      }
    }
  
    if (this.esEdicion && this.idProyectoEdicion) {
      this.proyectoService.actualizarProyecto(this.idProyectoEdicion, this.proyecto).subscribe({
        next: () => this.mostrarExito('Propiedad actualizada con éxito.'),
        error: (err) => this.mostrarError(err)
      });
    } else {
      this.proyectoService.crearProyecto(this.proyecto).subscribe({
        next: () => this.mostrarExito('Propiedad registrada en Fusagasugá.'),
        error: (err) => this.mostrarError(err)
      });
    }
  }

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