import { Profile } from './profile.model';

export interface Address {
  id: string;
  address: string;
  isDefault: boolean;
  isDeleted: boolean;
  fullName: string;
  phone: string;
  profile?: Profile; // Optional as it might be a nested object
}
