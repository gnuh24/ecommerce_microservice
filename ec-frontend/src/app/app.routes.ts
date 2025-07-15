import { Routes } from '@angular/router';
import { MainLayoutComponent } from './layouts/main-layout/main-layout.component';

export const routes: Routes = [
  {
    path: '',
    component: MainLayoutComponent, // Sử dụng MainLayoutComponent làm layout chính
    children: [
      { path: '', redirectTo: 'home', pathMatch: 'full' },
      {
        path: 'home',
        loadComponent: () =>
          import('./features/home/home.component').then((m) => m.HomeComponent), // Lazy load HomeComponent
      },
      {
        path: 'products',
        loadComponent: () =>
          import('./features/products/products.component').then((m) => m.ProductsComponent), // Lazy load ProductsComponent
      },
      {
        path: 'products/:slug',
        loadComponent: () =>
          import('./features/product/product-detail/product-detail.component').then((m) => m.ProductDetailComponent), // Lazy load ProductDetailComponent
      },
      // Thêm các route khác vào đây nếu cần
    ],
  },
  {
    path: 'auth',
    loadChildren: () =>
      import('./features/auth/auth-routing.module').then((m) => m.AuthRoutingModule),
  },
  { path: '**', redirectTo: 'home' },
];
