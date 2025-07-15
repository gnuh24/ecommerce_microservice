export interface ProductVariant {
  id: string;
  volume: number;
  price: number;
  quantity: number;
  isPublished: boolean;
  isDeleted: boolean;
  createdAt: Date;
  updatedAt: Date;
  deletedAt?: Date;
  // product: Product; // Avoid circular dependency, link by ID if needed
}
