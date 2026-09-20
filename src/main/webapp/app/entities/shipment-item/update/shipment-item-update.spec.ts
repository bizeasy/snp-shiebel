import { beforeEach, describe, expect, it, vi } from 'vitest';
import { HttpResponse } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';

import { provideTranslateService } from '@ngx-translate/core';
import { Subject, from, of } from 'rxjs';

import { IProduct } from 'app/entities/product/product.model';
import { ProductService } from 'app/entities/product/service/product.service';
import { ShipmentService } from 'app/entities/shipment/service/shipment.service';
import { IShipment } from 'app/entities/shipment/shipment.model';
import { ShipmentItemService } from '../service/shipment-item.service';
import { IShipmentItem } from '../shipment-item.model';

import { ShipmentItemFormService } from './shipment-item-form.service';
import { ShipmentItemUpdate } from './shipment-item-update';

describe('ShipmentItem Management Update Component', () => {
  let comp: ShipmentItemUpdate;
  let fixture: ComponentFixture<ShipmentItemUpdate>;
  let activatedRoute: ActivatedRoute;
  let shipmentItemFormService: ShipmentItemFormService;
  let shipmentItemService: ShipmentItemService;
  let shipmentService: ShipmentService;
  let productService: ProductService;

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

    fixture = TestBed.createComponent(ShipmentItemUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    shipmentItemFormService = TestBed.inject(ShipmentItemFormService);
    shipmentItemService = TestBed.inject(ShipmentItemService);
    shipmentService = TestBed.inject(ShipmentService);
    productService = TestBed.inject(ProductService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call Shipment query and add missing value', () => {
      const shipmentItem: IShipmentItem = { id: 27511 };
      const shipment: IShipment = { id: 9594 };
      shipmentItem.shipment = shipment;

      const shipmentCollection: IShipment[] = [{ id: 9594 }];
      vi.spyOn(shipmentService, 'query').mockReturnValue(of(new HttpResponse({ body: shipmentCollection })));
      const additionalShipments = [shipment];
      const expectedCollection: IShipment[] = [...additionalShipments, ...shipmentCollection];
      vi.spyOn(shipmentService, 'addShipmentToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ shipmentItem });
      comp.ngOnInit();

      expect(shipmentService.query).toHaveBeenCalled();
      expect(shipmentService.addShipmentToCollectionIfMissing).toHaveBeenCalledWith(
        shipmentCollection,
        ...additionalShipments.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.shipmentsSharedCollection()).toEqual(expectedCollection);
    });

    it('should call Product query and add missing value', () => {
      const shipmentItem: IShipmentItem = { id: 27511 };
      const product: IProduct = { id: 21536 };
      shipmentItem.product = product;

      const productCollection: IProduct[] = [{ id: 21536 }];
      vi.spyOn(productService, 'query').mockReturnValue(of(new HttpResponse({ body: productCollection })));
      const additionalProducts = [product];
      const expectedCollection: IProduct[] = [...additionalProducts, ...productCollection];
      vi.spyOn(productService, 'addProductToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ shipmentItem });
      comp.ngOnInit();

      expect(productService.query).toHaveBeenCalled();
      expect(productService.addProductToCollectionIfMissing).toHaveBeenCalledWith(
        productCollection,
        ...additionalProducts.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.productsSharedCollection()).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const shipmentItem: IShipmentItem = { id: 27511 };
      const shipment: IShipment = { id: 9594 };
      shipmentItem.shipment = shipment;
      const product: IProduct = { id: 21536 };
      shipmentItem.product = product;

      activatedRoute.data = of({ shipmentItem });
      comp.ngOnInit();

      expect(comp.shipmentsSharedCollection()).toContainEqual(shipment);
      expect(comp.productsSharedCollection()).toContainEqual(product);
      expect(comp.shipmentItem).toEqual(shipmentItem);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<IShipmentItem>();
      const shipmentItem = { id: 14901 };
      vi.spyOn(shipmentItemFormService, 'getShipmentItem').mockReturnValue(shipmentItem);
      vi.spyOn(shipmentItemService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ shipmentItem });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(shipmentItem);
      saveSubject.complete();

      // THEN
      expect(shipmentItemFormService.getShipmentItem).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(shipmentItemService.update).toHaveBeenCalledWith(expect.objectContaining(shipmentItem));
      expect(comp.isSaving()).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<IShipmentItem>();
      const shipmentItem = { id: 14901 };
      vi.spyOn(shipmentItemFormService, 'getShipmentItem').mockReturnValue({ id: null });
      vi.spyOn(shipmentItemService, 'create').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ shipmentItem: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(shipmentItem);
      saveSubject.complete();

      // THEN
      expect(shipmentItemFormService.getShipmentItem).toHaveBeenCalled();
      expect(shipmentItemService.create).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<IShipmentItem>();
      const shipmentItem = { id: 14901 };
      vi.spyOn(shipmentItemService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ shipmentItem });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(shipmentItemService.update).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareShipment', () => {
      it('should forward to shipmentService', () => {
        const entity = { id: 9594 };
        const entity2 = { id: 9190 };
        vi.spyOn(shipmentService, 'compareShipment');
        comp.compareShipment(entity, entity2);
        expect(shipmentService.compareShipment).toHaveBeenCalledWith(entity, entity2);
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
  });
});
