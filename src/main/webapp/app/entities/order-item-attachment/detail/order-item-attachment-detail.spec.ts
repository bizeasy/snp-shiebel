import { beforeEach, describe, expect, it, vi } from 'vitest';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';

import { FaIconLibrary } from '@fortawesome/angular-fontawesome';
import { faArrowLeft, faPencilAlt } from '@fortawesome/free-solid-svg-icons';
import { provideTranslateService } from '@ngx-translate/core';
import { of } from 'rxjs';

import { OrderItemAttachmentDetail } from './order-item-attachment-detail';

describe('OrderItemAttachment Management Detail Component', () => {
  let comp: OrderItemAttachmentDetail;
  let fixture: ComponentFixture<OrderItemAttachmentDetail>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        provideTranslateService(),
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./order-item-attachment-detail').then(m => m.OrderItemAttachmentDetail),
              resolve: { orderItemAttachment: () => of({ id: 4150 }) },
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
    fixture = TestBed.createComponent(OrderItemAttachmentDetail);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('should load orderItemAttachment on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', OrderItemAttachmentDetail);

      // THEN
      expect(instance.orderItemAttachment()).toEqual(expect.objectContaining({ id: 4150 }));
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
