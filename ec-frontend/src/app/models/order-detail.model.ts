import { Order } from './order.model';

export interface OrderDetail {
  id: number;
  order?: Order; // Optional to avoid circular dependency
  productVariantId: string;
  productName: string;
  productThumbnail?: string;
  productVolume?: number;
  unitPrice: number;
  quantity: number;
  totalPrice: number;
}
