import { beforeEach, describe, expect, it, vi } from 'vitest';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';

import { provideTranslateService } from '@ngx-translate/core';
import { Subject, from, of } from 'rxjs';

import { IInvoiceItemType } from '../invoice-item-type.model';
import { InvoiceItemTypeService } from '../service/invoice-item-type.service';

import { InvoiceItemTypeFormService } from './invoice-item-type-form.service';
import { InvoiceItemTypeUpdate } from './invoice-item-type-update';

describe('InvoiceItemType Management Update Component', () => {
  let comp: InvoiceItemTypeUpdate;
  let fixture: ComponentFixture<InvoiceItemTypeUpdate>;
  let activatedRoute: ActivatedRoute;
  let invoiceItemTypeFormService: InvoiceItemTypeFormService;
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

    fixture = TestBed.createComponent(InvoiceItemTypeUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    invoiceItemTypeFormService = TestBed.inject(InvoiceItemTypeFormService);
    invoiceItemTypeService = TestBed.inject(InvoiceItemTypeService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should update editForm', () => {
      const invoiceItemType: IInvoiceItemType = { id: 25434 };

      activatedRoute.data = of({ invoiceItemType });
      comp.ngOnInit();

      expect(comp.invoiceItemType).toEqual(invoiceItemType);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<IInvoiceItemType>();
      const invoiceItemType = { id: 32383 };
      vi.spyOn(invoiceItemTypeFormService, 'getInvoiceItemType').mockReturnValue(invoiceItemType);
      vi.spyOn(invoiceItemTypeService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ invoiceItemType });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(invoiceItemType);
      saveSubject.complete();

      // THEN
      expect(invoiceItemTypeFormService.getInvoiceItemType).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(invoiceItemTypeService.update).toHaveBeenCalledWith(expect.objectContaining(invoiceItemType));
      expect(comp.isSaving()).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<IInvoiceItemType>();
      const invoiceItemType = { id: 32383 };
      vi.spyOn(invoiceItemTypeFormService, 'getInvoiceItemType').mockReturnValue({ id: null });
      vi.spyOn(invoiceItemTypeService, 'create').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ invoiceItemType: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(invoiceItemType);
      saveSubject.complete();

      // THEN
      expect(invoiceItemTypeFormService.getInvoiceItemType).toHaveBeenCalled();
      expect(invoiceItemTypeService.create).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<IInvoiceItemType>();
      const invoiceItemType = { id: 32383 };
      vi.spyOn(invoiceItemTypeService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ invoiceItemType });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(invoiceItemTypeService.update).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
