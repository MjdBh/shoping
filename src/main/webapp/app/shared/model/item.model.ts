import dayjs from 'dayjs';
import { IShoppingGroup } from 'app/shared/model/shopping-group.model';
import { IPerson } from 'app/shared/model/person.model';
import { ItemState } from 'app/shared/model/enumerations/item-state.model';

export interface IItem {
  id?: number;
  name?: string;
  price?: number | null;
  createdAt?: string;
  picture?: string | null;
  state?: ItemState | null;
  group?: IShoppingGroup | null;
  owner?: IPerson | null;
  interestedPersons?: IPerson[] | null;
}

export const defaultValue: Readonly<IItem> = {};
