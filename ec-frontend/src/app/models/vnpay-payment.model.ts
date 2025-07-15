import { Payment } from './payment.model';
import { VnPayResponseCode } from '../enums/vnpay-response-code.enum';
import { VnPayTransactionStatusCode } from '../enums/vnpay-transaction-status-code.enum';

export interface VnPayPayment {
  id: string;
  payment?: Payment; // Optional to avoid circular dependency
  vnpResponseCode: VnPayResponseCode;
  vnpTransactionStatusCode: VnPayTransactionStatusCode;
  transactionId?: string;
  paymentTime?: Date;
  vnpSecureHash?: string;
  bankCode?: string;
  cardType?: string;
  vnpResponseStatus?: string;
  vnpTransactionStatus?: string;
}
