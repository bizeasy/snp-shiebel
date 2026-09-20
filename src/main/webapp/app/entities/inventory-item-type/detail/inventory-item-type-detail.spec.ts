import { beforeEach, describe, expect, it, vi } from 'vitest';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';

import { FaIconLibrary } from '@fortawesome/angular-fontawesome';
import { faArrowLeft, faPencilAlt } from '@fortawesome/free-solid-svg-icons';
import { provideTranslateService } from '@ngx-translate/core';
import { of } from 'rxjs';

import { InventoryItemTypeDetail } from './inventory-item-type-detail';

describe('InventoryItemType Management Detail Component', () => {
  let comp: InventoryItemTypeDetail;
  let fixture: ComponentFixture<InventoryItemTypeDetail>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        provideTranslateService(),
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./inventory-item-type-detail').then(m => m.InventoryItemTypeDetail),
              resolve: { inventoryItemType: () => of({ id: 9002 }) },
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
    fixture = TestBed.createComponent(InventoryItemTypeDetail);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('should load inventoryItemType on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', InventoryItemTypeDetail);

      // THEN
      expect(instance.inventoryItemType()).toEqual(expect.objectContaining({ id: 9002 }));
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
