import { Order } from './order.model';
import { PaymentStatus } from '../enums/payment-status.enum';
import { PaymentMethod } from '../enums/payment-method.enum';

export interface Payment {
  id: string;
  paymentStatus: PaymentStatus;
  paymentMethod: PaymentMethod;
  order?: Order; // Optional to avoid circular dependency
}
