import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { ProyectoService } from '../../services/propiedad'; 
import { ContabilidadService } from '../../services/contabilidad';




@Component({
  selector: 'app-contabilidad',
  standalone: true,
  imports: [CommonModule, RouterModule, FormsModule],
  templateUrl: './contabilidad.html',
  styleUrl: './contabilidad.css',
})
export class ContabilidadComponent implements OnInit {
  private graficoDona: any;
  movimientos: any[] = [];
  proyectos: any[] = [];
  proyectoSeleccionadoId: number = 0;
  mostrarGrafico: boolean = false;
  totalIngresos: number = 0;
  totalEgresos: number = 0;
  balanceCaja: number = 0;
  fechaMinima: string = new Date().toISOString().split('T')[0];
  transacciones: any[] = [];
  mostrarModal: boolean = false;

  nuevoMovimiento = {
    numeroFactura: '',
    fecha: new Date().toISOString().substring(0, 10),
    descripcion: '',
    socioResponsable: '',
    tipo: 'EGRESO', 
    valor: null,
    idProyecto: null 
  };

  constructor(
    private proyectoService: ProyectoService, 
    private contabilidadService: ContabilidadService, // 👈 CORREGIDO
    private cd: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.cargarProyectosDelBackend();
    this.cargarDatosContables(); 
  }

  cargarProyectosDelBackend(): void {
    this.proyectoService.listarProyectos().subscribe({
      next: (data) => {
        this.proyectos = data.map((p: any) => ({
          id: p.id_proyecto || p.id, 
          nombre: p.nombre
        }));
        this.cd.detectChanges(); 
      },
      error: (err) => {
        console.error('Error al cargar proyectos en el módulo de contabilidad:', err);
      }
    });
  }

  cargarDatosContables(): void {
    const idFiltrado = this.proyectoSeleccionadoId > 0 ? this.proyectoSeleccionadoId : undefined;
  
    // 1. Obtener Transacciones para la tabla
    this.contabilidadService.obtenerTransacciones(idFiltrado).subscribe({
      next: (res) => {
        this.transacciones = res || [];
        this.cd.detectChanges();
      },
      error: (err) => console.error('Error al obtener transacciones:', err)
    });
  
    // 2. Obtener Resumen para las tarjetas y el reporte gráfico
    this.contabilidadService.obtenerResumen(idFiltrado).subscribe({
      next: (resumen: any) => {
        this.totalIngresos = resumen?.ingresos || 0;
        this.totalEgresos = resumen?.egresos || 0;
        this.balanceCaja = resumen?.balance || 0;
        

        
        this.cd.detectChanges();
      },
      error: (err) => {
        console.error('Error al obtener resumen financiero:', err);
        this.totalIngresos = 0;
        this.totalEgresos = 0;
        this.balanceCaja = 0;
        

        
        this.cd.detectChanges();
      }
    });
  }

  onProyectoChange(event: any): void {
    const id = Number(event.target.value);
    this.proyectoSeleccionadoId = id;
    this.cargarDatosContables(); 
  }

  abrirModal(): void {
    this.mostrarModal = true;
    if (this.proyectoSeleccionadoId > 0) {
      this.nuevoMovimiento.idProyecto = this.proyectoSeleccionadoId as any;
    }
  }

  cerrarModal(): void {
    this.mostrarModal = false;
    this.nuevoMovimiento = {
      numeroFactura: '',
      fecha: new Date().toISOString().substring(0, 10),
      descripcion: '',
      socioResponsable: '',
      tipo: 'EGRESO',
      valor: null,
      idProyecto: null
    };
  }

  guardarMovimiento(): void {
    if (!this.nuevoMovimiento.numeroFactura || !this.nuevoMovimiento.valor || !this.nuevoMovimiento.idProyecto) {
      alert('Por favor, completa los campos requeridos (*)');
      return;
    }

    // Usamos el servicio inyectado para enviar los datos
    this.contabilidadService.registrarMovimiento(this.nuevoMovimiento).subscribe({
      next: () => {
        alert('Movimiento registrado con éxito');
        this.cerrarModal();
        // Recargamos los datos para actualizar las tarjetas y la tabla
        this.cargarDatosContables(); 
      },
      error: (err) => {
        console.error('Error al guardar movimiento:', err);
        alert('Ocurrió un error al registrar el movimiento.');
      }
    });
  }
  abrirGrafico(): void {
    this.mostrarGrafico = true;
  }
  
  cerrarGrafico(): void {
    this.mostrarGrafico = false;
  }

  get porcentajeEgresos(): number {
    if (this.totalIngresos === 0) return 0;
    return Math.min(100, (this.totalEgresos / this.totalIngresos) * 100);
  }
  public chartOptions = { 
    responsive: true, 
    maintainAspectRatio: false 
  };
  
  public donutData: any = { labels: [], datasets: [] };
  public barData: any = { labels: [], datasets: [] };
  
 


}