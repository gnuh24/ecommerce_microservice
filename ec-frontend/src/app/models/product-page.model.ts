export interface ProductListItem {
  id: string;
  productName: string;
  slug: string;
  thumbnailUrl: string;
  minPrice: number;
  maxPrice: number;
}

export interface Sort {
  empty: boolean;
  sorted: boolean;
  unsorted: boolean;
}

export interface Pageable {
  pageNumber: number;
  pageSize: number;
  sort: Sort;
  offset: number;
  unpaged: boolean;
  paged: boolean;
}

export interface ProductPageData {
  content: ProductListItem[];
  pageable: Pageable;
  last: boolean;
  totalElements: number;
  totalPages: number;
  size: number;
  number: number;
  sort: Sort;
  numberOfElements: number;
  first: boolean;
  empty: boolean;
}

export interface ProductListResponse {
  status: number;
  message: string;
  data: ProductPageData;
}
