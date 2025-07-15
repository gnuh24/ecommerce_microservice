import { Order } from './order.model';
import { OrderStatusEnum } from '../enums/order-status.enum';

export interface OrderStatus {
  id: number;
  order?: Order; // Optional to avoid circular dependency
  status: OrderStatusEnum;
  updateTime: Date;
}
