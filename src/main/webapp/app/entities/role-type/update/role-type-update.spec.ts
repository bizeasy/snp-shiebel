import { beforeEach, describe, expect, it, vi } from 'vitest';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';

import { provideTranslateService } from '@ngx-translate/core';
import { Subject, from, of } from 'rxjs';

import { IRoleType } from '../role-type.model';
import { RoleTypeService } from '../service/role-type.service';

import { RoleTypeFormService } from './role-type-form.service';
import { RoleTypeUpdate } from './role-type-update';

describe('RoleType Management Update Component', () => {
  let comp: RoleTypeUpdate;
  let fixture: ComponentFixture<RoleTypeUpdate>;
  let activatedRoute: ActivatedRoute;
  let roleTypeFormService: RoleTypeFormService;
  let roleTypeService: RoleTypeService;

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

    fixture = TestBed.createComponent(RoleTypeUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    roleTypeFormService = TestBed.inject(RoleTypeFormService);
    roleTypeService = TestBed.inject(RoleTypeService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should update editForm', () => {
      const roleType: IRoleType = { id: 27052 };

      activatedRoute.data = of({ roleType });
      comp.ngOnInit();

      expect(comp.roleType).toEqual(roleType);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<IRoleType>();
      const roleType = { id: 17576 };
      vi.spyOn(roleTypeFormService, 'getRoleType').mockReturnValue(roleType);
      vi.spyOn(roleTypeService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ roleType });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(roleType);
      saveSubject.complete();

      // THEN
      expect(roleTypeFormService.getRoleType).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(roleTypeService.update).toHaveBeenCalledWith(expect.objectContaining(roleType));
      expect(comp.isSaving()).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<IRoleType>();
      const roleType = { id: 17576 };
      vi.spyOn(roleTypeFormService, 'getRoleType').mockReturnValue({ id: null });
      vi.spyOn(roleTypeService, 'create').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ roleType: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(roleType);
      saveSubject.complete();

      // THEN
      expect(roleTypeFormService.getRoleType).toHaveBeenCalled();
      expect(roleTypeService.create).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<IRoleType>();
      const roleType = { id: 17576 };
      vi.spyOn(roleTypeService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ roleType });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(roleTypeService.update).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
