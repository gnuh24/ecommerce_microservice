export interface Brand {
  id: string;
  brandName: string;
  productCount: number;
  createdAt: Date;
  updatedAt: Date;
  deletedAt?: Date;
  isDeleted: boolean;
}
export interface BrandNoPagination {
  id: string;
  brandName: string;
  productCount: number;
}