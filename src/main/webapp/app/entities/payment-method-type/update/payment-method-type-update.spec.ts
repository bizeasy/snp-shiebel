import { beforeEach, describe, expect, it, vi } from 'vitest';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';

import { provideTranslateService } from '@ngx-translate/core';
import { Subject, from, of } from 'rxjs';

import { IPaymentMethodType } from '../payment-method-type.model';
import { PaymentMethodTypeService } from '../service/payment-method-type.service';

import { PaymentMethodTypeFormService } from './payment-method-type-form.service';
import { PaymentMethodTypeUpdate } from './payment-method-type-update';

describe('PaymentMethodType Management Update Component', () => {
  let comp: PaymentMethodTypeUpdate;
  let fixture: ComponentFixture<PaymentMethodTypeUpdate>;
  let activatedRoute: ActivatedRoute;
  let paymentMethodTypeFormService: PaymentMethodTypeFormService;
  let paymentMethodTypeService: PaymentMethodTypeService;

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

    fixture = TestBed.createComponent(PaymentMethodTypeUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    paymentMethodTypeFormService = TestBed.inject(PaymentMethodTypeFormService);
    paymentMethodTypeService = TestBed.inject(PaymentMethodTypeService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should update editForm', () => {
      const paymentMethodType: IPaymentMethodType = { id: 13122 };

      activatedRoute.data = of({ paymentMethodType });
      comp.ngOnInit();

      expect(comp.paymentMethodType).toEqual(paymentMethodType);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<IPaymentMethodType>();
      const paymentMethodType = { id: 6662 };
      vi.spyOn(paymentMethodTypeFormService, 'getPaymentMethodType').mockReturnValue(paymentMethodType);
      vi.spyOn(paymentMethodTypeService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ paymentMethodType });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(paymentMethodType);
      saveSubject.complete();

      // THEN
      expect(paymentMethodTypeFormService.getPaymentMethodType).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(paymentMethodTypeService.update).toHaveBeenCalledWith(expect.objectContaining(paymentMethodType));
      expect(comp.isSaving()).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<IPaymentMethodType>();
      const paymentMethodType = { id: 6662 };
      vi.spyOn(paymentMethodTypeFormService, 'getPaymentMethodType').mockReturnValue({ id: null });
      vi.spyOn(paymentMethodTypeService, 'create').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ paymentMethodType: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(paymentMethodType);
      saveSubject.complete();

      // THEN
      expect(paymentMethodTypeFormService.getPaymentMethodType).toHaveBeenCalled();
      expect(paymentMethodTypeService.create).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<IPaymentMethodType>();
      const paymentMethodType = { id: 6662 };
      vi.spyOn(paymentMethodTypeService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ paymentMethodType });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(paymentMethodTypeService.update).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
