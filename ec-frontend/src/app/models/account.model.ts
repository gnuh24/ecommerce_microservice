import { Profile } from './profile.model';
import { Role } from '../enums/role.enum';
import { Status } from '../enums/status.enum';

export interface Account {
  id: string;
  createdAt: Date;
  updatedAt: Date;
  username: string;
  password?: string; // Password should not be sent to frontend normally
  role: Role;
  status: Status;
  profile: Profile;
}
