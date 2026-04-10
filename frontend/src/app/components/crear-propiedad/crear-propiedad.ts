import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterModule, Router } from '@angular/router';
// CORRECCIÓN: Importamos ProyectoService (el nombre real de la clase)
import { ProyectoService } from '../../services/propiedad'; 

@Component({
  selector: 'app-crear-propiedad',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: './crear-propiedad.html'
})
export class CrearPropiedadComponent {
  proyecto = {
    nombre: '',
    descripcion: '',
    fecha_inicio: '',
    fecha_fin: '',
    id_usuario_admin: 11 // ID de Sebastian por defecto
  };

  constructor(
    // CORRECCIÓN: Usamos ProyectoService aquí también
    private proyectoService: ProyectoService, 
    private router: Router
  ) {}

  guardar() {
    this.proyectoService.crearProyecto(this.proyecto).subscribe({
      // CORRECCIÓN: Agregamos tipo 'any' para evitar errores TS7006
      next: (res: any) => {
        alert('Propiedad guardada con éxito');
        this.router.navigate(['/explorar']);
      },
      error: (err: any) => {
        console.error('Error al guardar', err);
        alert('Error al conectar con el servidor');
      }
    });
  }
}