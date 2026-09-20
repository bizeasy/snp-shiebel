import { HttpResponse } from '@angular/common/http';
import { Component, OnInit, inject, signal } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { Observable, finalize, map } from 'rxjs';

import { IProduct } from 'app/entities/product/product.model';
import { ProductService } from 'app/entities/product/service/product.service';
import { ShoppingListService } from 'app/entities/shopping-list/service/shopping-list.service';
import { IShoppingList } from 'app/entities/shopping-list/shopping-list.model';
import { StatusService } from 'app/entities/status/service/status.service';
import { IStatus } from 'app/entities/status/status.model';
import { AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';
import { ShoppingListItemService } from '../service/shopping-list-item.service';
import { IShoppingListItem } from '../shopping-list-item.model';

import { ShoppingListItemFormGroup, ShoppingListItemFormService } from './shopping-list-item-form.service';

@Component({
  selector: 'sys-shopping-list-item-update',
  templateUrl: './shopping-list-item-update.html',
  imports: [TranslateDirective, FontAwesomeModule, AlertError, ReactiveFormsModule],
})
export class ShoppingListItemUpdate implements OnInit {
  readonly isSaving = signal(false);
  shoppingListItem: IShoppingListItem | null = null;

  shoppingListsSharedCollection = signal<IShoppingList[]>([]);
  productsSharedCollection = signal<IProduct[]>([]);
  statusesSharedCollection = signal<IStatus[]>([]);

  protected shoppingListItemService = inject(ShoppingListItemService);
  protected shoppingListItemFormService = inject(ShoppingListItemFormService);
  protected shoppingListService = inject(ShoppingListService);
  protected productService = inject(ProductService);
  protected statusService = inject(StatusService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: ShoppingListItemFormGroup = this.shoppingListItemFormService.createShoppingListItemFormGroup();

  compareShoppingList = (o1: IShoppingList | null, o2: IShoppingList | null): boolean =>
    this.shoppingListService.compareShoppingList(o1, o2);

  compareProduct = (o1: IProduct | null, o2: IProduct | null): boolean => this.productService.compareProduct(o1, o2);

  compareStatus = (o1: IStatus | null, o2: IStatus | null): boolean => this.statusService.compareStatus(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ shoppingListItem }) => {
      this.shoppingListItem = shoppingListItem;
      if (shoppingListItem) {
        this.updateForm(shoppingListItem);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    globalThis.history.back();
  }

  save(): void {
    this.isSaving.set(true);
    const shoppingListItem = this.shoppingListItemFormService.getShoppingListItem(this.editForm);
    if (shoppingListItem.id === null) {
      this.subscribeToSaveResponse(this.shoppingListItemService.create(shoppingListItem));
    } else {
      this.subscribeToSaveResponse(this.shoppingListItemService.update(shoppingListItem));
    }
  }

  protected subscribeToSaveResponse(result: Observable<IShoppingListItem | null>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving.set(false);
  }

  protected updateForm(shoppingListItem: IShoppingListItem): void {
    this.shoppingListItem = shoppingListItem;
    this.shoppingListItemFormService.resetForm(this.editForm, shoppingListItem);

    this.shoppingListsSharedCollection.update(shoppingLists =>
      this.shoppingListService.addShoppingListToCollectionIfMissing<IShoppingList>(shoppingLists, shoppingListItem.shoppingList),
    );
    this.productsSharedCollection.update(products =>
      this.productService.addProductToCollectionIfMissing<IProduct>(products, shoppingListItem.product),
    );
    this.statusesSharedCollection.update(statuses =>
      this.statusService.addStatusToCollectionIfMissing<IStatus>(statuses, shoppingListItem.status),
    );
  }

  protected loadRelationshipsOptions(): void {
    this.shoppingListService
      .query()
      .pipe(map((res: HttpResponse<IShoppingList[]>) => res.body ?? []))
      .pipe(
        map((shoppingLists: IShoppingList[]) =>
          this.shoppingListService.addShoppingListToCollectionIfMissing<IShoppingList>(shoppingLists, this.shoppingListItem?.shoppingList),
        ),
      )
      .subscribe((shoppingLists: IShoppingList[]) => this.shoppingListsSharedCollection.set(shoppingLists));

    this.productService
      .query()
      .pipe(map((res: HttpResponse<IProduct[]>) => res.body ?? []))
      .pipe(
        map((products: IProduct[]) =>
          this.productService.addProductToCollectionIfMissing<IProduct>(products, this.shoppingListItem?.product),
        ),
      )
      .subscribe((products: IProduct[]) => this.productsSharedCollection.set(products));

    this.statusService
      .query()
      .pipe(map((res: HttpResponse<IStatus[]>) => res.body ?? []))
      .pipe(
        map((statuses: IStatus[]) => this.statusService.addStatusToCollectionIfMissing<IStatus>(statuses, this.shoppingListItem?.status)),
      )
      .subscribe((statuses: IStatus[]) => this.statusesSharedCollection.set(statuses));
  }
}
