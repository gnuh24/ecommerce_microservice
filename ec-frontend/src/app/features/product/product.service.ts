import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Product, ProductDetail, Page } from './product.dto';
import { environment } from '../../../enviro/environment';

@Injectable({
  providedIn: 'root'
})
export class ProductService {
  private apiUrl = environment.apiUrl + '/catalog/products';

  constructor(private http: HttpClient) { }

  getPublicProducts(page: number, size: number, search: string = ''): Observable<Page<Product>> {
    let params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString());

    if (search) {
      params = params.set('search', search);
    }

    return this.http.get<Page<Product>>(`${this.apiUrl}/public`, { params });
  }

  getPublicProductBySlug(slug: string): Observable<ProductDetail> {
    return this.http.get<ProductDetail>(`${this.apiUrl}/public/${slug}`);
  }
}
