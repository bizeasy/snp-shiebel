import { beforeEach, describe, expect, it, vi } from 'vitest';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';

import { FaIconLibrary } from '@fortawesome/angular-fontawesome';
import { faArrowLeft, faPencilAlt } from '@fortawesome/free-solid-svg-icons';
import { provideTranslateService } from '@ngx-translate/core';
import { of } from 'rxjs';

import { PaymentGatewayConfigDetail } from './payment-gateway-config-detail';

describe('PaymentGatewayConfig Management Detail Component', () => {
  let comp: PaymentGatewayConfigDetail;
  let fixture: ComponentFixture<PaymentGatewayConfigDetail>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        provideTranslateService(),
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./payment-gateway-config-detail').then(m => m.PaymentGatewayConfigDetail),
              resolve: { paymentGatewayConfig: () => of({ id: 28126 }) },
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
    fixture = TestBed.createComponent(PaymentGatewayConfigDetail);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('should load paymentGatewayConfig on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', PaymentGatewayConfigDetail);

      // THEN
      expect(instance.paymentGatewayConfig()).toEqual(expect.objectContaining({ id: 28126 }));
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
