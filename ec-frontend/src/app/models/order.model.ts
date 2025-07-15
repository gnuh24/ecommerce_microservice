import { Payment } from './payment.model';
import { OrderDetail } from './order-detail.model';
import { OrderStatus } from './order-status.model';

export interface Order {
  id: string;
  totalAmount: number;
  note?: string;
  orderTime: Date;
  receiverName: string;
  receiverPhone: string;
  receiverAddress: string;
  isTemp: boolean;
  accountId: string;
  payments: Payment[];
  orderDetails: OrderDetail[];
  statuses: OrderStatus[];
}
