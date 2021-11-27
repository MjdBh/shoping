import dayjs from 'dayjs';
import { IShoppingGroup } from 'app/shared/model/shopping-group.model';
import { IItem } from 'app/shared/model/item.model';
import { PersonRole } from 'app/shared/model/enumerations/person-role.model';

export interface IPerson {
  id?: number;
  username?: string;
  name?: string | null;
  role?: PersonRole | null;
  createdAt?: string;
  shoppingGroups?: IShoppingGroup[] | null;
  items?: IItem[] | null;
  interests?: IItem[] | null;
  subscriptions?: IShoppingGroup[] | null;
  joineds?: IShoppingGroup[] | null;
}

export const defaultValue: Readonly<IPerson> = {};
