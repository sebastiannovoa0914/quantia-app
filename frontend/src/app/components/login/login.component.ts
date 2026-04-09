import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms'; 
import { Router } from "@angular/router";
import { AuthService } from "../../services/auth.service";

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './login.component.html'
})
export class LoginComponent {
    credentials = { email: '', contrasena: '' }; 
  
    constructor(private authService: AuthService, private router: Router) {}
  
    onLogin() {
      this.authService.login(this.credentials).subscribe({
        next: (res) => {
          // Si el servicio ya guardó los datos en el 'tap'
          if (res && res.token) {
            console.log('Login exitoso, redirigiendo...');
            this.router.navigate(['/home']);
          }
        },
        error: (err) => {
          console.error('Error capturado:', err);
          alert('Credenciales incorrectas');
        }
      });
    }
}