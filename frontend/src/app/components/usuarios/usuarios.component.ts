import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { HttpClient } from '@angular/common/http';
import { RouterModule } from '@angular/router';

@Component({
  selector: 'app-usuarios',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: './usuarios.html',
  styleUrl: './usuarios.css'
})
export class UsuariosComponent {
  
  // Objeto que coincide con tu modelo de Java
  nuevoPropietario = {
    nombre: '',
    email: '',
    contrasena: ''
  };

  mostrarModal: boolean = false;

  constructor(private http: HttpClient) {}

  abrirModal() {
    this.mostrarModal = true;
  }

  cerrarModal() {
    this.mostrarModal = false;
    this.nuevoPropietario = { nombre: '', email: '', contrasena: '' };
  }

  guardarPropietario() {
    // 1. Validación básica
    if (!this.nuevoPropietario.nombre || !this.nuevoPropietario.email || !this.nuevoPropietario.contrasena) {
      alert('Por favor, completa todos los campos.');
      return;
    }
  
    // 2. Preparamos el objeto incluyendo 'activo' como lo espera Java
    const datosAEnviar = {
      ...this.nuevoPropietario, // copia nombre, email y contrasena
      activo: true             // agregamos el valor que el backend necesita
    };
  
    // 3. Llamada al endpoint con el objeto completo
    this.http.post('http://localhost:8080/api/funciones/propietario', datosAEnviar)
      .subscribe({
        next: (res) => {
          alert('¡Propietario creado con éxito!');
          this.cerrarModal();
        },
        error: (err) => {
          console.error(err);
          alert('Error al crear el propietario. Revisa que el email no esté duplicado.');
        }
      });
  }
}