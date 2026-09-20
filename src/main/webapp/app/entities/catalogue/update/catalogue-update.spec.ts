import { beforeEach, describe, expect, it, vi } from 'vitest';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';

import { provideTranslateService } from '@ngx-translate/core';
import { Subject, from, of } from 'rxjs';

import { ICatalogue } from '../catalogue.model';
import { CatalogueService } from '../service/catalogue.service';

import { CatalogueFormService } from './catalogue-form.service';
import { CatalogueUpdate } from './catalogue-update';

describe('Catalogue Management Update Component', () => {
  let comp: CatalogueUpdate;
  let fixture: ComponentFixture<CatalogueUpdate>;
  let activatedRoute: ActivatedRoute;
  let catalogueFormService: CatalogueFormService;
  let catalogueService: CatalogueService;

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

    fixture = TestBed.createComponent(CatalogueUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    catalogueFormService = TestBed.inject(CatalogueFormService);
    catalogueService = TestBed.inject(CatalogueService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should update editForm', () => {
      const catalogue: ICatalogue = { id: 28253 };

      activatedRoute.data = of({ catalogue });
      comp.ngOnInit();

      expect(comp.catalogue).toEqual(catalogue);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<ICatalogue>();
      const catalogue = { id: 7498 };
      vi.spyOn(catalogueFormService, 'getCatalogue').mockReturnValue(catalogue);
      vi.spyOn(catalogueService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ catalogue });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(catalogue);
      saveSubject.complete();

      // THEN
      expect(catalogueFormService.getCatalogue).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(catalogueService.update).toHaveBeenCalledWith(expect.objectContaining(catalogue));
      expect(comp.isSaving()).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<ICatalogue>();
      const catalogue = { id: 7498 };
      vi.spyOn(catalogueFormService, 'getCatalogue').mockReturnValue({ id: null });
      vi.spyOn(catalogueService, 'create').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ catalogue: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(catalogue);
      saveSubject.complete();

      // THEN
      expect(catalogueFormService.getCatalogue).toHaveBeenCalled();
      expect(catalogueService.create).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<ICatalogue>();
      const catalogue = { id: 7498 };
      vi.spyOn(catalogueService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ catalogue });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(catalogueService.update).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
