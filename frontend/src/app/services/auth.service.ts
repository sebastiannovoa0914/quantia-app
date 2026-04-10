import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, tap } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class AuthService {
  private apiUrl = 'http://localhost:8080';

  constructor(private http: HttpClient) {}

  login(credentials: any): Observable<any> {
    return this.http.post(`${this.apiUrl}/auth/login`, credentials).pipe(
      tap((res: any) => { 
        console.log('Respuesta cruda del servidor:', res);
        if (res && res.token) {
          localStorage.setItem('token', res.token);
          localStorage.setItem('nombre', res.nombre || ''); 
          localStorage.setItem('rol', res.rol || '');
          
          // --- ESTA ES LA LÍNEA QUE TE FALTABA ---
          // Guardamos el ID que viene del Backend (AuthController)
          localStorage.setItem('id_usuario', res.id); 
          
          console.log('Datos guardados en LocalStorage, incluyendo ID:', res.id);
        }
      })
    );
  }

  register(userData: any): Observable<any> {
    const body = {
      nombre: userData.nombre,
      email: userData.email,
      contrasena: userData.contrasena,
      rol: 'ADMINISTRADOR', 
      activo: true
    };
    return this.http.post(`${this.apiUrl}/auth/register`, body);
  }

  getProfile(): Observable<any> {
    return this.http.get(`${this.apiUrl}/home/perfil`);
  }

  logout() { 
    localStorage.clear(); 
    console.log('Sesión cerrada y storage limpio');
  }
}