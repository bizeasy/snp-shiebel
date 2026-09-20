import { HttpResponse } from '@angular/common/http';
import { Component, OnInit, inject, signal } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { Observable, finalize, map } from 'rxjs';

import { ICustRequest } from 'app/entities/cust-request/cust-request.model';
import { CustRequestService } from 'app/entities/cust-request/service/cust-request.service';
import { IProduct } from 'app/entities/product/product.model';
import { ProductService } from 'app/entities/product/service/product.service';
import { ShoppingListItemService } from 'app/entities/shopping-list-item/service/shopping-list-item.service';
import { IShoppingListItem } from 'app/entities/shopping-list-item/shopping-list-item.model';
import { StatusService } from 'app/entities/status/service/status.service';
import { IStatus } from 'app/entities/status/status.model';
import { UomService } from 'app/entities/uom/service/uom.service';
import { IUom } from 'app/entities/uom/uom.model';
import { AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';
import { ICustRequestItem } from '../cust-request-item.model';
import { CustRequestItemService } from '../service/cust-request-item.service';

import { CustRequestItemFormGroup, CustRequestItemFormService } from './cust-request-item-form.service';

@Component({
  selector: 'sys-cust-request-item-update',
  templateUrl: './cust-request-item-update.html',
  imports: [TranslateDirective, FontAwesomeModule, AlertError, ReactiveFormsModule],
})
export class CustRequestItemUpdate implements OnInit {
  readonly isSaving = signal(false);
  custRequestItem: ICustRequestItem | null = null;

  custRequestsSharedCollection = signal<ICustRequest[]>([]);
  statusesSharedCollection = signal<IStatus[]>([]);
  productsSharedCollection = signal<IProduct[]>([]);
  uomsSharedCollection = signal<IUom[]>([]);
  shoppingListItemsSharedCollection = signal<IShoppingListItem[]>([]);

  protected custRequestItemService = inject(CustRequestItemService);
  protected custRequestItemFormService = inject(CustRequestItemFormService);
  protected custRequestService = inject(CustRequestService);
  protected statusService = inject(StatusService);
  protected productService = inject(ProductService);
  protected uomService = inject(UomService);
  protected shoppingListItemService = inject(ShoppingListItemService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: CustRequestItemFormGroup = this.custRequestItemFormService.createCustRequestItemFormGroup();

  compareCustRequest = (o1: ICustRequest | null, o2: ICustRequest | null): boolean => this.custRequestService.compareCustRequest(o1, o2);

  compareStatus = (o1: IStatus | null, o2: IStatus | null): boolean => this.statusService.compareStatus(o1, o2);

  compareProduct = (o1: IProduct | null, o2: IProduct | null): boolean => this.productService.compareProduct(o1, o2);

  compareUom = (o1: IUom | null, o2: IUom | null): boolean => this.uomService.compareUom(o1, o2);

  compareShoppingListItem = (o1: IShoppingListItem | null, o2: IShoppingListItem | null): boolean =>
    this.shoppingListItemService.compareShoppingListItem(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ custRequestItem }) => {
      this.custRequestItem = custRequestItem;
      if (custRequestItem) {
        this.updateForm(custRequestItem);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    globalThis.history.back();
  }

  save(): void {
    this.isSaving.set(true);
    const custRequestItem = this.custRequestItemFormService.getCustRequestItem(this.editForm);
    if (custRequestItem.id === null) {
      this.subscribeToSaveResponse(this.custRequestItemService.create(custRequestItem));
    } else {
      this.subscribeToSaveResponse(this.custRequestItemService.update(custRequestItem));
    }
  }

  protected subscribeToSaveResponse(result: Observable<ICustRequestItem | null>): void {
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

  protected updateForm(custRequestItem: ICustRequestItem): void {
    this.custRequestItem = custRequestItem;
    this.custRequestItemFormService.resetForm(this.editForm, custRequestItem);

    this.custRequestsSharedCollection.update(custRequests =>
      this.custRequestService.addCustRequestToCollectionIfMissing<ICustRequest>(custRequests, custRequestItem.custRequest),
    );
    this.statusesSharedCollection.update(statuses =>
      this.statusService.addStatusToCollectionIfMissing<IStatus>(statuses, custRequestItem.status),
    );
    this.productsSharedCollection.update(products =>
      this.productService.addProductToCollectionIfMissing<IProduct>(products, custRequestItem.product),
    );
    this.uomsSharedCollection.update(uoms => this.uomService.addUomToCollectionIfMissing<IUom>(uoms, custRequestItem.uom));
    this.shoppingListItemsSharedCollection.update(shoppingListItems =>
      this.shoppingListItemService.addShoppingListItemToCollectionIfMissing<IShoppingListItem>(
        shoppingListItems,
        custRequestItem.shoppingListItem,
      ),
    );
  }

  protected loadRelationshipsOptions(): void {
    this.custRequestService
      .query()
      .pipe(map((res: HttpResponse<ICustRequest[]>) => res.body ?? []))
      .pipe(
        map((custRequests: ICustRequest[]) =>
          this.custRequestService.addCustRequestToCollectionIfMissing<ICustRequest>(custRequests, this.custRequestItem?.custRequest),
        ),
      )
      .subscribe((custRequests: ICustRequest[]) => this.custRequestsSharedCollection.set(custRequests));

    this.statusService
      .query()
      .pipe(map((res: HttpResponse<IStatus[]>) => res.body ?? []))
      .pipe(
        map((statuses: IStatus[]) => this.statusService.addStatusToCollectionIfMissing<IStatus>(statuses, this.custRequestItem?.status)),
      )
      .subscribe((statuses: IStatus[]) => this.statusesSharedCollection.set(statuses));

    this.productService
      .query()
      .pipe(map((res: HttpResponse<IProduct[]>) => res.body ?? []))
      .pipe(
        map((products: IProduct[]) =>
          this.productService.addProductToCollectionIfMissing<IProduct>(products, this.custRequestItem?.product),
        ),
      )
      .subscribe((products: IProduct[]) => this.productsSharedCollection.set(products));

    this.uomService
      .query()
      .pipe(map((res: HttpResponse<IUom[]>) => res.body ?? []))
      .pipe(map((uoms: IUom[]) => this.uomService.addUomToCollectionIfMissing<IUom>(uoms, this.custRequestItem?.uom)))
      .subscribe((uoms: IUom[]) => this.uomsSharedCollection.set(uoms));

    this.shoppingListItemService
      .query()
      .pipe(map((res: HttpResponse<IShoppingListItem[]>) => res.body ?? []))
      .pipe(
        map((shoppingListItems: IShoppingListItem[]) =>
          this.shoppingListItemService.addShoppingListItemToCollectionIfMissing<IShoppingListItem>(
            shoppingListItems,
            this.custRequestItem?.shoppingListItem,
          ),
        ),
      )
      .subscribe((shoppingListItems: IShoppingListItem[]) => this.shoppingListItemsSharedCollection.set(shoppingListItems));
  }
}
