import { beforeEach, describe, expect, it, vi } from 'vitest';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';

import { provideTranslateService } from '@ngx-translate/core';
import { Subject, from, of } from 'rxjs';

import { IInvoiceType } from '../invoice-type.model';
import { InvoiceTypeService } from '../service/invoice-type.service';

import { InvoiceTypeFormService } from './invoice-type-form.service';
import { InvoiceTypeUpdate } from './invoice-type-update';

describe('InvoiceType Management Update Component', () => {
  let comp: InvoiceTypeUpdate;
  let fixture: ComponentFixture<InvoiceTypeUpdate>;
  let activatedRoute: ActivatedRoute;
  let invoiceTypeFormService: InvoiceTypeFormService;
  let invoiceTypeService: InvoiceTypeService;

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

    fixture = TestBed.createComponent(InvoiceTypeUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    invoiceTypeFormService = TestBed.inject(InvoiceTypeFormService);
    invoiceTypeService = TestBed.inject(InvoiceTypeService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should update editForm', () => {
      const invoiceType: IInvoiceType = { id: 3910 };

      activatedRoute.data = of({ invoiceType });
      comp.ngOnInit();

      expect(comp.invoiceType).toEqual(invoiceType);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<IInvoiceType>();
      const invoiceType = { id: 15913 };
      vi.spyOn(invoiceTypeFormService, 'getInvoiceType').mockReturnValue(invoiceType);
      vi.spyOn(invoiceTypeService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ invoiceType });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(invoiceType);
      saveSubject.complete();

      // THEN
      expect(invoiceTypeFormService.getInvoiceType).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(invoiceTypeService.update).toHaveBeenCalledWith(expect.objectContaining(invoiceType));
      expect(comp.isSaving()).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<IInvoiceType>();
      const invoiceType = { id: 15913 };
      vi.spyOn(invoiceTypeFormService, 'getInvoiceType').mockReturnValue({ id: null });
      vi.spyOn(invoiceTypeService, 'create').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ invoiceType: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(invoiceType);
      saveSubject.complete();

      // THEN
      expect(invoiceTypeFormService.getInvoiceType).toHaveBeenCalled();
      expect(invoiceTypeService.create).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<IInvoiceType>();
      const invoiceType = { id: 15913 };
      vi.spyOn(invoiceTypeService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ invoiceType });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(invoiceTypeService.update).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
