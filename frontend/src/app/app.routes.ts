import { Routes } from '@angular/router';

// Importaciones siguiendo tu estructura de carpetas exacta
import { LoginComponent } from './components/login/login.component';
import { RegistroComponent } from './components/registro/registro'; 
import { HomeComponent } from './components/home/home.component';
import { CrearPropiedad } from './components/crear-propiedad/crear-propiedad'; 
import { ExplorarComponent } from './components/explorar/explorar'; 

export const routes: Routes = [
  { path: 'login', component: LoginComponent },
  { path: 'registro', component: RegistroComponent },
  { path: 'home', component: HomeComponent },
  { path: 'crear-propiedad', component: CrearPropiedad },
  { path: 'explorar', component: ExplorarComponent }, 
  
  { path: '', redirectTo: 'login', pathMatch: 'full' },
  { path: '**', redirectTo: 'login' }
];