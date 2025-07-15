export interface Category {
  id: string;
  categoryName: string;
  productCount: number;
  createdAt: Date;
  updatedAt: Date;
  deletedAt?: Date;
  isDeleted: boolean;
}
export interface CategoryNoPagination {
  id: string;
  categoryName: string;
  productCount: number;
}
