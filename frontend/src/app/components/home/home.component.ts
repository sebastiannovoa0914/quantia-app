import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterModule } from '@angular/router'; // Añadimos RouterModule
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-home',
  standalone: true,
  // IMPORTANTE: CommonModule para el *ngIf y RouterModule para el [routerLink]
  imports: [CommonModule, RouterModule], 
  templateUrl: './home.component.html'
})
export class HomeComponent implements OnInit {
  nombreUsuario: string = '';
  rolUsuario: string = '';

  constructor(private authService: AuthService, private router: Router) {}

  ngOnInit(): void {
    // 1. Recuperamos los datos del localStorage
    const nombre = localStorage.getItem('nombre');
    const rol = localStorage.getItem('rol');
    const token = localStorage.getItem('token');

    // 2. Seguridad: Si no hay token, al login de una
    if (!token) {
      this.router.navigate(['/login']);
      return;
    }

    // 3. Asignamos y limpiamos valores
    // El .trim() quita espacios y .toUpperCase() asegura que sea ADMINISTRADOR
    this.nombreUsuario = nombre ? nombre : 'Usuario';
    this.rolUsuario = rol ? rol.trim().toUpperCase() : 'SIN ROL';

    // Consola para que tú mismo verifiques en F12 si el rol llegó bien
    console.log('Sesión iniciada como:', this.nombreUsuario);
    console.log('Rol detectado:', this.rolUsuario);
  }

  logout(): void {
    this.authService.logout();
    this.router.navigate(['/login']);
  }
}