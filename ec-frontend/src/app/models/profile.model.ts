import { Account } from './account.model';
import { Address } from './address.model';

export interface Profile {
  id: string;
  email: string;
  phone?: string;
  fullName?: string;
  birthday?: Date;
  gender?: string;
  account?: Account; // Optional to avoid circular dependency issues if not needed
  addresses?: Address[];
}
