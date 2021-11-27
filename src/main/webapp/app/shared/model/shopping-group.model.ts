import dayjs from 'dayjs';
import { IItem } from 'app/shared/model/item.model';
import { IPerson } from 'app/shared/model/person.model';

export interface IShoppingGroup {
  id?: number;
  name?: string;
  createdAt?: string;
  items?: IItem[] | null;
  createdBy?: IPerson | null;
  subscribedPersons?: IPerson[] | null;
  joinedPersons?: IPerson[] | null;
}

export const defaultValue: Readonly<IShoppingGroup> = {};
