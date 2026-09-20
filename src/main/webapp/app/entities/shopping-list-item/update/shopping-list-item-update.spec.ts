import { beforeEach, describe, expect, it, vi } from 'vitest';
import { HttpResponse } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';

import { provideTranslateService } from '@ngx-translate/core';
import { Subject, from, of } from 'rxjs';

import { IProduct } from 'app/entities/product/product.model';
import { ProductService } from 'app/entities/product/service/product.service';
import { ShoppingListService } from 'app/entities/shopping-list/service/shopping-list.service';
import { IShoppingList } from 'app/entities/shopping-list/shopping-list.model';
import { StatusService } from 'app/entities/status/service/status.service';
import { IStatus } from 'app/entities/status/status.model';
import { ShoppingListItemService } from '../service/shopping-list-item.service';
import { IShoppingListItem } from '../shopping-list-item.model';

import { ShoppingListItemFormService } from './shopping-list-item-form.service';
import { ShoppingListItemUpdate } from './shopping-list-item-update';

describe('ShoppingListItem Management Update Component', () => {
  let comp: ShoppingListItemUpdate;
  let fixture: ComponentFixture<ShoppingListItemUpdate>;
  let activatedRoute: ActivatedRoute;
  let shoppingListItemFormService: ShoppingListItemFormService;
  let shoppingListItemService: ShoppingListItemService;
  let shoppingListService: ShoppingListService;
  let productService: ProductService;
  let statusService: StatusService;

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

    fixture = TestBed.createComponent(ShoppingListItemUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    shoppingListItemFormService = TestBed.inject(ShoppingListItemFormService);
    shoppingListItemService = TestBed.inject(ShoppingListItemService);
    shoppingListService = TestBed.inject(ShoppingListService);
    productService = TestBed.inject(ProductService);
    statusService = TestBed.inject(StatusService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call ShoppingList query and add missing value', () => {
      const shoppingListItem: IShoppingListItem = { id: 6768 };
      const shoppingList: IShoppingList = { id: 18255 };
      shoppingListItem.shoppingList = shoppingList;

      const shoppingListCollection: IShoppingList[] = [{ id: 18255 }];
      vi.spyOn(shoppingListService, 'query').mockReturnValue(of(new HttpResponse({ body: shoppingListCollection })));
      const additionalShoppingLists = [shoppingList];
      const expectedCollection: IShoppingList[] = [...additionalShoppingLists, ...shoppingListCollection];
      vi.spyOn(shoppingListService, 'addShoppingListToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ shoppingListItem });
      comp.ngOnInit();

      expect(shoppingListService.query).toHaveBeenCalled();
      expect(shoppingListService.addShoppingListToCollectionIfMissing).toHaveBeenCalledWith(
        shoppingListCollection,
        ...additionalShoppingLists.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.shoppingListsSharedCollection()).toEqual(expectedCollection);
    });

    it('should call Product query and add missing value', () => {
      const shoppingListItem: IShoppingListItem = { id: 6768 };
      const product: IProduct = { id: 21536 };
      shoppingListItem.product = product;

      const productCollection: IProduct[] = [{ id: 21536 }];
      vi.spyOn(productService, 'query').mockReturnValue(of(new HttpResponse({ body: productCollection })));
      const additionalProducts = [product];
      const expectedCollection: IProduct[] = [...additionalProducts, ...productCollection];
      vi.spyOn(productService, 'addProductToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ shoppingListItem });
      comp.ngOnInit();

      expect(productService.query).toHaveBeenCalled();
      expect(productService.addProductToCollectionIfMissing).toHaveBeenCalledWith(
        productCollection,
        ...additionalProducts.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.productsSharedCollection()).toEqual(expectedCollection);
    });

    it('should call Status query and add missing value', () => {
      const shoppingListItem: IShoppingListItem = { id: 6768 };
      const status: IStatus = { id: 2927 };
      shoppingListItem.status = status;

      const statusCollection: IStatus[] = [{ id: 2927 }];
      vi.spyOn(statusService, 'query').mockReturnValue(of(new HttpResponse({ body: statusCollection })));
      const additionalStatuses = [status];
      const expectedCollection: IStatus[] = [...additionalStatuses, ...statusCollection];
      vi.spyOn(statusService, 'addStatusToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ shoppingListItem });
      comp.ngOnInit();

      expect(statusService.query).toHaveBeenCalled();
      expect(statusService.addStatusToCollectionIfMissing).toHaveBeenCalledWith(
        statusCollection,
        ...additionalStatuses.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.statusesSharedCollection()).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const shoppingListItem: IShoppingListItem = { id: 6768 };
      const shoppingList: IShoppingList = { id: 18255 };
      shoppingListItem.shoppingList = shoppingList;
      const product: IProduct = { id: 21536 };
      shoppingListItem.product = product;
      const status: IStatus = { id: 2927 };
      shoppingListItem.status = status;

      activatedRoute.data = of({ shoppingListItem });
      comp.ngOnInit();

      expect(comp.shoppingListsSharedCollection()).toContainEqual(shoppingList);
      expect(comp.productsSharedCollection()).toContainEqual(product);
      expect(comp.statusesSharedCollection()).toContainEqual(status);
      expect(comp.shoppingListItem).toEqual(shoppingListItem);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<IShoppingListItem>();
      const shoppingListItem = { id: 26675 };
      vi.spyOn(shoppingListItemFormService, 'getShoppingListItem').mockReturnValue(shoppingListItem);
      vi.spyOn(shoppingListItemService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ shoppingListItem });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(shoppingListItem);
      saveSubject.complete();

      // THEN
      expect(shoppingListItemFormService.getShoppingListItem).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(shoppingListItemService.update).toHaveBeenCalledWith(expect.objectContaining(shoppingListItem));
      expect(comp.isSaving()).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<IShoppingListItem>();
      const shoppingListItem = { id: 26675 };
      vi.spyOn(shoppingListItemFormService, 'getShoppingListItem').mockReturnValue({ id: null });
      vi.spyOn(shoppingListItemService, 'create').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ shoppingListItem: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(shoppingListItem);
      saveSubject.complete();

      // THEN
      expect(shoppingListItemFormService.getShoppingListItem).toHaveBeenCalled();
      expect(shoppingListItemService.create).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<IShoppingListItem>();
      const shoppingListItem = { id: 26675 };
      vi.spyOn(shoppingListItemService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ shoppingListItem });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(shoppingListItemService.update).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareShoppingList', () => {
      it('should forward to shoppingListService', () => {
        const entity = { id: 18255 };
        const entity2 = { id: 11571 };
        vi.spyOn(shoppingListService, 'compareShoppingList');
        comp.compareShoppingList(entity, entity2);
        expect(shoppingListService.compareShoppingList).toHaveBeenCalledWith(entity, entity2);
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

    describe('compareStatus', () => {
      it('should forward to statusService', () => {
        const entity = { id: 2927 };
        const entity2 = { id: 22202 };
        vi.spyOn(statusService, 'compareStatus');
        comp.compareStatus(entity, entity2);
        expect(statusService.compareStatus).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
