import { Category } from './category.model';
import { Brand } from './brand.model';
import { ProductVariant } from './product-variant.model';
import { ProductImage } from './product-image.model';

export interface Product {
  id: string;
  productName: string;
  slug: string;
  description?: string;
  vintage?: number;
  alcohol?: number;
  region?: string;
  isPublished: boolean;
  createdAt: Date;
  updatedAt: Date;
  deletedAt?: Date;
  isDeleted: boolean;
  category: Category;
  brand: Brand;
  variants: ProductVariant[];
  images: ProductImage[];
}
export interface AllProducts {
  id: string;
  productName: string;
  slug: string;
  thumbnail?: string;
  minPrice: number;
  maxPrice: number;
}
