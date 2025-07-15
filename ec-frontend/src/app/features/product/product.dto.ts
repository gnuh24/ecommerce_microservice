export interface Product {
  id: string;
  productName: string;
  slug: string;
  thumbnailUrl: string;
  minPrice: number;
  maxPrice: number;
}

export interface ProductDetail extends Product {
  description: string;
  isInWishlist: boolean;
  // Add other properties from the backend DTO as needed
}

export interface Page<T> {
  content: T[];
  totalPages: number;
  totalElements: number;
  size: number;
  number: number;
}
