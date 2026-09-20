import { beforeEach, describe, expect, it, vi } from 'vitest';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';

import { FaIconLibrary } from '@fortawesome/angular-fontawesome';
import { faArrowLeft, faPencilAlt } from '@fortawesome/free-solid-svg-icons';
import { provideTranslateService } from '@ngx-translate/core';
import { of } from 'rxjs';

import { PaymentMethodTypeDetail } from './payment-method-type-detail';

describe('PaymentMethodType Management Detail Component', () => {
  let comp: PaymentMethodTypeDetail;
  let fixture: ComponentFixture<PaymentMethodTypeDetail>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        provideTranslateService(),
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./payment-method-type-detail').then(m => m.PaymentMethodTypeDetail),
              resolve: { paymentMethodType: () => of({ id: 6662 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    });
    const library = TestBed.inject(FaIconLibrary);
    library.addIcons(faArrowLeft);
    library.addIcons(faPencilAlt);
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(PaymentMethodTypeDetail);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('should load paymentMethodType on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', PaymentMethodTypeDetail);

      // THEN
      expect(instance.paymentMethodType()).toEqual(expect.objectContaining({ id: 6662 }));
    });
  });

  describe('PreviousState', () => {
    it('should navigate to previous state', () => {
      vi.spyOn(globalThis.history, 'back');
      comp.previousState();
      expect(globalThis.history.back).toHaveBeenCalled();
    });
  });
});
