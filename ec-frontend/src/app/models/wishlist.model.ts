import { Account } from './account.model';
import { Product } from './product.model';

export interface WishlistId {
  accountId: string;
  productId: string;
}

export interface Wishlist {
  id: WishlistId;
  createdAt: Date;
  account?: Account; // Optional to avoid circular dependency
  product?: Product; // Optional to avoid circular dependency
}
