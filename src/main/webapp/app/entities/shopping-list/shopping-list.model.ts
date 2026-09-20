import { IParty } from 'app/entities/party/party.model';
import { IShoppingListType } from 'app/entities/shopping-list-type/shopping-list-type.model';
import { IStatus } from 'app/entities/status/status.model';

export interface IShoppingList {
  id: number;
  name?: string | null;
  isActive?: boolean | null;
  shoppingListType?: IShoppingListType | null;
  party?: IParty | null;
  status?: IStatus | null;
}

export type NewShoppingList = Omit<IShoppingList, 'id'> & { id: null };
