import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ProyectoService { // Cambiado de PropiedadService a ProyectoService
  private apiUrl = 'http://localhost:8080/api/proyectos'; 

  constructor(private http: HttpClient) {}

  // Método para el listado (Explorar)
  listarProyectos(): Observable<any[]> {
    const token = localStorage.getItem('token');
    const headers = new HttpHeaders({
      'Authorization': `Bearer ${token}`
    });
    return this.http.get<any[]>(this.apiUrl, { headers });
  }

  // Tu método de crear que ya tenías
  crearProyecto(proyecto: any): Observable<any> {
    const token = localStorage.getItem('token');
    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${token}`
    });
    return this.http.post(this.apiUrl, proyecto, { headers });
  }
}