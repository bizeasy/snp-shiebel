import { beforeEach, describe, expect, it, vi } from 'vitest';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';

import { provideTranslateService } from '@ngx-translate/core';
import { Subject, from, of } from 'rxjs';

import { IPaymentType } from '../payment-type.model';
import { PaymentTypeService } from '../service/payment-type.service';

import { PaymentTypeFormService } from './payment-type-form.service';
import { PaymentTypeUpdate } from './payment-type-update';

describe('PaymentType Management Update Component', () => {
  let comp: PaymentTypeUpdate;
  let fixture: ComponentFixture<PaymentTypeUpdate>;
  let activatedRoute: ActivatedRoute;
  let paymentTypeFormService: PaymentTypeFormService;
  let paymentTypeService: PaymentTypeService;

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

    fixture = TestBed.createComponent(PaymentTypeUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    paymentTypeFormService = TestBed.inject(PaymentTypeFormService);
    paymentTypeService = TestBed.inject(PaymentTypeService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should update editForm', () => {
      const paymentType: IPaymentType = { id: 8193 };

      activatedRoute.data = of({ paymentType });
      comp.ngOnInit();

      expect(comp.paymentType).toEqual(paymentType);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<IPaymentType>();
      const paymentType = { id: 3410 };
      vi.spyOn(paymentTypeFormService, 'getPaymentType').mockReturnValue(paymentType);
      vi.spyOn(paymentTypeService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ paymentType });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(paymentType);
      saveSubject.complete();

      // THEN
      expect(paymentTypeFormService.getPaymentType).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(paymentTypeService.update).toHaveBeenCalledWith(expect.objectContaining(paymentType));
      expect(comp.isSaving()).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<IPaymentType>();
      const paymentType = { id: 3410 };
      vi.spyOn(paymentTypeFormService, 'getPaymentType').mockReturnValue({ id: null });
      vi.spyOn(paymentTypeService, 'create').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ paymentType: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(paymentType);
      saveSubject.complete();

      // THEN
      expect(paymentTypeFormService.getPaymentType).toHaveBeenCalled();
      expect(paymentTypeService.create).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<IPaymentType>();
      const paymentType = { id: 3410 };
      vi.spyOn(paymentTypeService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ paymentType });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(paymentTypeService.update).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
