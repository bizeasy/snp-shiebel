import {
  entityConfirmDeleteButtonSelector,
  entityCreateButtonSelector,
  entityCreateCancelButtonSelector,
  entityCreateSaveButtonSelector,
  entityDeleteButtonSelector,
  entityDetailsBackButtonSelector,
  entityDetailsButtonSelector,
  entityEditButtonSelector,
  entityTableSelector,
} from '../../support/entity';

describe('RoleType e2e test', () => {
  const roleTypePageUrl = '/role-type';
  let username: string;
  let password: string;
  const roleTypeSample = { name: 'spear bogus' };

  let roleType;

  before(() => {
    cy.credentials().then(credentials => {
      ({ username, password } = credentials);
    });
  });

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/role-types+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/role-types').as('postEntityRequest');
    cy.intercept('DELETE', '/api/role-types/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (roleType) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/role-types/${roleType.id}`,
      }).then(() => {
        roleType = undefined;
      });
    }
  });

  it('RoleTypes menu should load RoleTypes page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('role-type');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('RoleType').should('exist');
    cy.location('pathname').should('eq', roleTypePageUrl);
  });

  describe('RoleType page', () => {
    it('should have translated page title', () => {
      cy.visit(roleTypePageUrl);
      cy.getEntityHeading('RoleType').should('not.contain', 'snpApp.roleType.home.title');
    });

    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(roleTypePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create RoleType page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.location('pathname').should('eq', `${roleTypePageUrl}/new`);
        cy.getEntityCreateUpdateHeading('RoleType');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', roleTypePageUrl);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/role-types',
          body: roleTypeSample,
        }).then(({ body }) => {
          roleType = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/role-types+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [roleType],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(roleTypePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details RoleType page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('roleType');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', roleTypePageUrl);
      });

      it('edit button click should load edit RoleType page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('RoleType');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', roleTypePageUrl);
      });

      it('edit button click should load edit RoleType page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('RoleType');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', roleTypePageUrl);
      });

      it('last delete button click should delete instance of RoleType', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('roleType').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', roleTypePageUrl);

        roleType = undefined;
      });
    });
  });

  describe('new RoleType page', () => {
    beforeEach(() => {
      cy.visit(roleTypePageUrl);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('RoleType');
    });

    it('should create an instance of RoleType', () => {
      cy.get(`[data-cy="name"]`).type('as');
      cy.get(`[data-cy="name"]`).should('have.value', 'as');

      cy.get(`[data-cy="description"]`).type('yum');
      cy.get(`[data-cy="description"]`).should('have.value', 'yum');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        roleType = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.location('pathname').should('eq', roleTypePageUrl);
    });
  });
});
