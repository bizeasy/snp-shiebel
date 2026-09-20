import { beforeEach, describe, expect, it, vi } from 'vitest';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';

import { FaIconLibrary } from '@fortawesome/angular-fontawesome';
import { faArrowLeft, faPencilAlt } from '@fortawesome/free-solid-svg-icons';
import { provideTranslateService } from '@ngx-translate/core';
import { of } from 'rxjs';

import { OrderAdjustmentTypeDetail } from './order-adjustment-type-detail';

describe('OrderAdjustmentType Management Detail Component', () => {
  let comp: OrderAdjustmentTypeDetail;
  let fixture: ComponentFixture<OrderAdjustmentTypeDetail>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        provideTranslateService(),
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./order-adjustment-type-detail').then(m => m.OrderAdjustmentTypeDetail),
              resolve: { orderAdjustmentType: () => of({ id: 1453 }) },
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
    fixture = TestBed.createComponent(OrderAdjustmentTypeDetail);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('should load orderAdjustmentType on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', OrderAdjustmentTypeDetail);

      // THEN
      expect(instance.orderAdjustmentType()).toEqual(expect.objectContaining({ id: 1453 }));
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
