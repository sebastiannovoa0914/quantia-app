import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
// Importamos 'ProyectoService' desde el archivo 'propiedad'
import { ProyectoService } from '../../services/propiedad'; 

@Component({
  selector: 'app-explorar',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './explorar.html'
})
export class ExplorarComponent implements OnInit {
  proyectos: any[] = [];

  constructor(private proyectoService: ProyectoService) {}

  ngOnInit(): void {
    this.obtenerPropiedades();
  }

  obtenerPropiedades(): void {
    this.proyectoService.listarProyectos().subscribe({
      next: (data) => {
        this.proyectos = data;
        console.log('Datos de MySQL:', data);
      },
      error: (err) => console.error('Error al conectar con el Backend', err)
    });
  }
}