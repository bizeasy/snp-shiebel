import { beforeEach, describe, expect, it, vi } from 'vitest';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';

import { FaIconLibrary } from '@fortawesome/angular-fontawesome';
import { faArrowLeft, faPencilAlt } from '@fortawesome/free-solid-svg-icons';
import { provideTranslateService } from '@ngx-translate/core';
import { of } from 'rxjs';

import { PaymentGatewayResponseDetail } from './payment-gateway-response-detail';

describe('PaymentGatewayResponse Management Detail Component', () => {
  let comp: PaymentGatewayResponseDetail;
  let fixture: ComponentFixture<PaymentGatewayResponseDetail>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        provideTranslateService(),
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./payment-gateway-response-detail').then(m => m.PaymentGatewayResponseDetail),
              resolve: { paymentGatewayResponse: () => of({ id: 25625 }) },
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
    fixture = TestBed.createComponent(PaymentGatewayResponseDetail);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('should load paymentGatewayResponse on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', PaymentGatewayResponseDetail);

      // THEN
      expect(instance.paymentGatewayResponse()).toEqual(expect.objectContaining({ id: 25625 }));
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
