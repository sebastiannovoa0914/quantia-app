import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, Router } from '@angular/router';
import { FormsModule } from '@angular/forms'; // Necesario para vincular los inputs
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-registro',
  standalone: true,
  imports: [CommonModule, RouterModule, FormsModule],
  templateUrl: './registro.html',
  styleUrl: './registro.css',
})
export class RegistroComponent {
  // Este objeto debe tener los nombres que usaremos en el [(ngModel)] del HTML
  nuevoUsuario = {
    nombre: '',
    email: '',
    contrasena: ''
  };

  constructor(private authService: AuthService, private router: Router) {}

  onSubmit() {
    // Validamos que no envíe campos vacíos
    if (!this.nuevoUsuario.nombre || !this.nuevoUsuario.email || !this.nuevoUsuario.contrasena) {
      alert('Por favor, completa todos los campos.');
      return;
    }

    console.log('Enviando datos al servidor...', this.nuevoUsuario);

    this.authService.register(this.nuevoUsuario).subscribe({
      next: (res) => {
        console.log('Registro exitoso en MySQL:', res);
        alert('¡Usuario registrado con éxito! Ahora puedes iniciar sesión.');
        this.router.navigate(['/login']); // Te manda al login automáticamente
      },
      error: (err) => {
        console.error('Error al registrar:', err);
        // Manejo de errores básico para la presentación
        if (err.status === 409) {
          alert('El correo ya está registrado.');
        } else {
          alert('Error de conexión con el servidor.');
        }
      }
    });
  }
}