import { beforeEach, describe, expect, it, vi } from 'vitest';
import { HttpResponse } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';

import { provideTranslateService } from '@ngx-translate/core';
import { Subject, from, of } from 'rxjs';

import { IInvoiceItemType } from 'app/entities/invoice-item-type/invoice-item-type.model';
import { InvoiceItemTypeService } from 'app/entities/invoice-item-type/service/invoice-item-type.service';
import { IInvoiceType } from 'app/entities/invoice-type/invoice-type.model';
import { InvoiceTypeService } from 'app/entities/invoice-type/service/invoice-type.service';
import { IInvoiceItemTypeMap } from '../invoice-item-type-map.model';
import { InvoiceItemTypeMapService } from '../service/invoice-item-type-map.service';

import { InvoiceItemTypeMapFormService } from './invoice-item-type-map-form.service';
import { InvoiceItemTypeMapUpdate } from './invoice-item-type-map-update';

describe('InvoiceItemTypeMap Management Update Component', () => {
  let comp: InvoiceItemTypeMapUpdate;
  let fixture: ComponentFixture<InvoiceItemTypeMapUpdate>;
  let activatedRoute: ActivatedRoute;
  let invoiceItemTypeMapFormService: InvoiceItemTypeMapFormService;
  let invoiceItemTypeMapService: InvoiceItemTypeMapService;
  let invoiceTypeService: InvoiceTypeService;
  let invoiceItemTypeService: InvoiceItemTypeService;

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

    fixture = TestBed.createComponent(InvoiceItemTypeMapUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    invoiceItemTypeMapFormService = TestBed.inject(InvoiceItemTypeMapFormService);
    invoiceItemTypeMapService = TestBed.inject(InvoiceItemTypeMapService);
    invoiceTypeService = TestBed.inject(InvoiceTypeService);
    invoiceItemTypeService = TestBed.inject(InvoiceItemTypeService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call InvoiceType query and add missing value', () => {
      const invoiceItemTypeMap: IInvoiceItemTypeMap = { id: 10124 };
      const invoiceType: IInvoiceType = { id: 15913 };
      invoiceItemTypeMap.invoiceType = invoiceType;

      const invoiceTypeCollection: IInvoiceType[] = [{ id: 15913 }];
      vi.spyOn(invoiceTypeService, 'query').mockReturnValue(of(new HttpResponse({ body: invoiceTypeCollection })));
      const additionalInvoiceTypes = [invoiceType];
      const expectedCollection: IInvoiceType[] = [...additionalInvoiceTypes, ...invoiceTypeCollection];
      vi.spyOn(invoiceTypeService, 'addInvoiceTypeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ invoiceItemTypeMap });
      comp.ngOnInit();

      expect(invoiceTypeService.query).toHaveBeenCalled();
      expect(invoiceTypeService.addInvoiceTypeToCollectionIfMissing).toHaveBeenCalledWith(
        invoiceTypeCollection,
        ...additionalInvoiceTypes.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.invoiceTypesSharedCollection()).toEqual(expectedCollection);
    });

    it('should call InvoiceItemType query and add missing value', () => {
      const invoiceItemTypeMap: IInvoiceItemTypeMap = { id: 10124 };
      const invoiceItemType: IInvoiceItemType = { id: 32383 };
      invoiceItemTypeMap.invoiceItemType = invoiceItemType;

      const invoiceItemTypeCollection: IInvoiceItemType[] = [{ id: 32383 }];
      vi.spyOn(invoiceItemTypeService, 'query').mockReturnValue(of(new HttpResponse({ body: invoiceItemTypeCollection })));
      const additionalInvoiceItemTypes = [invoiceItemType];
      const expectedCollection: IInvoiceItemType[] = [...additionalInvoiceItemTypes, ...invoiceItemTypeCollection];
      vi.spyOn(invoiceItemTypeService, 'addInvoiceItemTypeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ invoiceItemTypeMap });
      comp.ngOnInit();

      expect(invoiceItemTypeService.query).toHaveBeenCalled();
      expect(invoiceItemTypeService.addInvoiceItemTypeToCollectionIfMissing).toHaveBeenCalledWith(
        invoiceItemTypeCollection,
        ...additionalInvoiceItemTypes.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.invoiceItemTypesSharedCollection()).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const invoiceItemTypeMap: IInvoiceItemTypeMap = { id: 10124 };
      const invoiceType: IInvoiceType = { id: 15913 };
      invoiceItemTypeMap.invoiceType = invoiceType;
      const invoiceItemType: IInvoiceItemType = { id: 32383 };
      invoiceItemTypeMap.invoiceItemType = invoiceItemType;

      activatedRoute.data = of({ invoiceItemTypeMap });
      comp.ngOnInit();

      expect(comp.invoiceTypesSharedCollection()).toContainEqual(invoiceType);
      expect(comp.invoiceItemTypesSharedCollection()).toContainEqual(invoiceItemType);
      expect(comp.invoiceItemTypeMap).toEqual(invoiceItemTypeMap);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<IInvoiceItemTypeMap>();
      const invoiceItemTypeMap = { id: 18575 };
      vi.spyOn(invoiceItemTypeMapFormService, 'getInvoiceItemTypeMap').mockReturnValue(invoiceItemTypeMap);
      vi.spyOn(invoiceItemTypeMapService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ invoiceItemTypeMap });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(invoiceItemTypeMap);
      saveSubject.complete();

      // THEN
      expect(invoiceItemTypeMapFormService.getInvoiceItemTypeMap).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(invoiceItemTypeMapService.update).toHaveBeenCalledWith(expect.objectContaining(invoiceItemTypeMap));
      expect(comp.isSaving()).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<IInvoiceItemTypeMap>();
      const invoiceItemTypeMap = { id: 18575 };
      vi.spyOn(invoiceItemTypeMapFormService, 'getInvoiceItemTypeMap').mockReturnValue({ id: null });
      vi.spyOn(invoiceItemTypeMapService, 'create').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ invoiceItemTypeMap: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(invoiceItemTypeMap);
      saveSubject.complete();

      // THEN
      expect(invoiceItemTypeMapFormService.getInvoiceItemTypeMap).toHaveBeenCalled();
      expect(invoiceItemTypeMapService.create).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<IInvoiceItemTypeMap>();
      const invoiceItemTypeMap = { id: 18575 };
      vi.spyOn(invoiceItemTypeMapService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ invoiceItemTypeMap });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(invoiceItemTypeMapService.update).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareInvoiceType', () => {
      it('should forward to invoiceTypeService', () => {
        const entity = { id: 15913 };
        const entity2 = { id: 3910 };
        vi.spyOn(invoiceTypeService, 'compareInvoiceType');
        comp.compareInvoiceType(entity, entity2);
        expect(invoiceTypeService.compareInvoiceType).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareInvoiceItemType', () => {
      it('should forward to invoiceItemTypeService', () => {
        const entity = { id: 32383 };
        const entity2 = { id: 25434 };
        vi.spyOn(invoiceItemTypeService, 'compareInvoiceItemType');
        comp.compareInvoiceItemType(entity, entity2);
        expect(invoiceItemTypeService.compareInvoiceItemType).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
