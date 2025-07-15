export interface ProductImage {
  id: string;
  imageUrl: string;
  isThumbnail: boolean;
  isDeleted: boolean;
  createdAt: Date;
  deletedAt?: Date;
  // product: Product; // Avoid circular dependency, link by ID if needed
}
