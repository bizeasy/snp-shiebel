import { beforeEach, describe, expect, it, vi } from 'vitest';
import { HttpResponse } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';

import { provideTranslateService } from '@ngx-translate/core';
import { Subject, from, of } from 'rxjs';

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
import { ICustRequestItem } from '../cust-request-item.model';
import { CustRequestItemService } from '../service/cust-request-item.service';

import { CustRequestItemFormService } from './cust-request-item-form.service';
import { CustRequestItemUpdate } from './cust-request-item-update';

describe('CustRequestItem Management Update Component', () => {
  let comp: CustRequestItemUpdate;
  let fixture: ComponentFixture<CustRequestItemUpdate>;
  let activatedRoute: ActivatedRoute;
  let custRequestItemFormService: CustRequestItemFormService;
  let custRequestItemService: CustRequestItemService;
  let custRequestService: CustRequestService;
  let statusService: StatusService;
  let productService: ProductService;
  let uomService: UomService;
  let shoppingListItemService: ShoppingListItemService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        provideTranslateService(),
        provideHttpClientTesting(),
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    });

    fixture = TestBed.createComponent(CustRequestItemUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    custRequestItemFormService = TestBed.inject(CustRequestItemFormService);
    custRequestItemService = TestBed.inject(CustRequestItemService);
    custRequestService = TestBed.inject(CustRequestService);
    statusService = TestBed.inject(StatusService);
    productService = TestBed.inject(ProductService);
    uomService = TestBed.inject(UomService);
    shoppingListItemService = TestBed.inject(ShoppingListItemService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call CustRequest query and add missing value', () => {
      const custRequestItem: ICustRequestItem = { id: 23718 };
      const custRequest: ICustRequest = { id: 17760 };
      custRequestItem.custRequest = custRequest;

      const custRequestCollection: ICustRequest[] = [{ id: 17760 }];
      vi.spyOn(custRequestService, 'query').mockReturnValue(of(new HttpResponse({ body: custRequestCollection })));
      const additionalCustRequests = [custRequest];
      const expectedCollection: ICustRequest[] = [...additionalCustRequests, ...custRequestCollection];
      vi.spyOn(custRequestService, 'addCustRequestToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ custRequestItem });
      comp.ngOnInit();

      expect(custRequestService.query).toHaveBeenCalled();
      expect(custRequestService.addCustRequestToCollectionIfMissing).toHaveBeenCalledWith(
        custRequestCollection,
        ...additionalCustRequests.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.custRequestsSharedCollection()).toEqual(expectedCollection);
    });

    it('should call Status query and add missing value', () => {
      const custRequestItem: ICustRequestItem = { id: 23718 };
      const status: IStatus = { id: 2927 };
      custRequestItem.status = status;

      const statusCollection: IStatus[] = [{ id: 2927 }];
      vi.spyOn(statusService, 'query').mockReturnValue(of(new HttpResponse({ body: statusCollection })));
      const additionalStatuses = [status];
      const expectedCollection: IStatus[] = [...additionalStatuses, ...statusCollection];
      vi.spyOn(statusService, 'addStatusToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ custRequestItem });
      comp.ngOnInit();

      expect(statusService.query).toHaveBeenCalled();
      expect(statusService.addStatusToCollectionIfMissing).toHaveBeenCalledWith(
        statusCollection,
        ...additionalStatuses.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.statusesSharedCollection()).toEqual(expectedCollection);
    });

    it('should call Product query and add missing value', () => {
      const custRequestItem: ICustRequestItem = { id: 23718 };
      const product: IProduct = { id: 21536 };
      custRequestItem.product = product;

      const productCollection: IProduct[] = [{ id: 21536 }];
      vi.spyOn(productService, 'query').mockReturnValue(of(new HttpResponse({ body: productCollection })));
      const additionalProducts = [product];
      const expectedCollection: IProduct[] = [...additionalProducts, ...productCollection];
      vi.spyOn(productService, 'addProductToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ custRequestItem });
      comp.ngOnInit();

      expect(productService.query).toHaveBeenCalled();
      expect(productService.addProductToCollectionIfMissing).toHaveBeenCalledWith(
        productCollection,
        ...additionalProducts.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.productsSharedCollection()).toEqual(expectedCollection);
    });

    it('should call Uom query and add missing value', () => {
      const custRequestItem: ICustRequestItem = { id: 23718 };
      const uom: IUom = { id: 6576 };
      custRequestItem.uom = uom;

      const uomCollection: IUom[] = [{ id: 6576 }];
      vi.spyOn(uomService, 'query').mockReturnValue(of(new HttpResponse({ body: uomCollection })));
      const additionalUoms = [uom];
      const expectedCollection: IUom[] = [...additionalUoms, ...uomCollection];
      vi.spyOn(uomService, 'addUomToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ custRequestItem });
      comp.ngOnInit();

      expect(uomService.query).toHaveBeenCalled();
      expect(uomService.addUomToCollectionIfMissing).toHaveBeenCalledWith(
        uomCollection,
        ...additionalUoms.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.uomsSharedCollection()).toEqual(expectedCollection);
    });

    it('should call ShoppingListItem query and add missing value', () => {
      const custRequestItem: ICustRequestItem = { id: 23718 };
      const shoppingListItem: IShoppingListItem = { id: 26675 };
      custRequestItem.shoppingListItem = shoppingListItem;

      const shoppingListItemCollection: IShoppingListItem[] = [{ id: 26675 }];
      vi.spyOn(shoppingListItemService, 'query').mockReturnValue(of(new HttpResponse({ body: shoppingListItemCollection })));
      const additionalShoppingListItems = [shoppingListItem];
      const expectedCollection: IShoppingListItem[] = [...additionalShoppingListItems, ...shoppingListItemCollection];
      vi.spyOn(shoppingListItemService, 'addShoppingListItemToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ custRequestItem });
      comp.ngOnInit();

      expect(shoppingListItemService.query).toHaveBeenCalled();
      expect(shoppingListItemService.addShoppingListItemToCollectionIfMissing).toHaveBeenCalledWith(
        shoppingListItemCollection,
        ...additionalShoppingListItems.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.shoppingListItemsSharedCollection()).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const custRequestItem: ICustRequestItem = { id: 23718 };
      const custRequest: ICustRequest = { id: 17760 };
      custRequestItem.custRequest = custRequest;
      const status: IStatus = { id: 2927 };
      custRequestItem.status = status;
      const product: IProduct = { id: 21536 };
      custRequestItem.product = product;
      const uom: IUom = { id: 6576 };
      custRequestItem.uom = uom;
      const shoppingListItem: IShoppingListItem = { id: 26675 };
      custRequestItem.shoppingListItem = shoppingListItem;

      activatedRoute.data = of({ custRequestItem });
      comp.ngOnInit();

      expect(comp.custRequestsSharedCollection()).toContainEqual(custRequest);
      expect(comp.statusesSharedCollection()).toContainEqual(status);
      expect(comp.productsSharedCollection()).toContainEqual(product);
      expect(comp.uomsSharedCollection()).toContainEqual(uom);
      expect(comp.shoppingListItemsSharedCollection()).toContainEqual(shoppingListItem);
      expect(comp.custRequestItem).toEqual(custRequestItem);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<ICustRequestItem>();
      const custRequestItem = { id: 17328 };
      vi.spyOn(custRequestItemFormService, 'getCustRequestItem').mockReturnValue(custRequestItem);
      vi.spyOn(custRequestItemService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ custRequestItem });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(custRequestItem);
      saveSubject.complete();

      // THEN
      expect(custRequestItemFormService.getCustRequestItem).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(custRequestItemService.update).toHaveBeenCalledWith(expect.objectContaining(custRequestItem));
      expect(comp.isSaving()).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<ICustRequestItem>();
      const custRequestItem = { id: 17328 };
      vi.spyOn(custRequestItemFormService, 'getCustRequestItem').mockReturnValue({ id: null });
      vi.spyOn(custRequestItemService, 'create').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ custRequestItem: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(custRequestItem);
      saveSubject.complete();

      // THEN
      expect(custRequestItemFormService.getCustRequestItem).toHaveBeenCalled();
      expect(custRequestItemService.create).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<ICustRequestItem>();
      const custRequestItem = { id: 17328 };
      vi.spyOn(custRequestItemService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ custRequestItem });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(custRequestItemService.update).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareCustRequest', () => {
      it('should forward to custRequestService', () => {
        const entity = { id: 17760 };
        const entity2 = { id: 10199 };
        vi.spyOn(custRequestService, 'compareCustRequest');
        comp.compareCustRequest(entity, entity2);
        expect(custRequestService.compareCustRequest).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareStatus', () => {
      it('should forward to statusService', () => {
        const entity = { id: 2927 };
        const entity2 = { id: 22202 };
        vi.spyOn(statusService, 'compareStatus');
        comp.compareStatus(entity, entity2);
        expect(statusService.compareStatus).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareProduct', () => {
      it('should forward to productService', () => {
        const entity = { id: 21536 };
        const entity2 = { id: 11926 };
        vi.spyOn(productService, 'compareProduct');
        comp.compareProduct(entity, entity2);
        expect(productService.compareProduct).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareUom', () => {
      it('should forward to uomService', () => {
        const entity = { id: 6576 };
        const entity2 = { id: 20168 };
        vi.spyOn(uomService, 'compareUom');
        comp.compareUom(entity, entity2);
        expect(uomService.compareUom).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareShoppingListItem', () => {
      it('should forward to shoppingListItemService', () => {
        const entity = { id: 26675 };
        const entity2 = { id: 6768 };
        vi.spyOn(shoppingListItemService, 'compareShoppingListItem');
        comp.compareShoppingListItem(entity, entity2);
        expect(shoppingListItemService.compareShoppingListItem).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
