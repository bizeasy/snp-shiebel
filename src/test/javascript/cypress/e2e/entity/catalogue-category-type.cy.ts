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

describe('CatalogueCategoryType e2e test', () => {
  const catalogueCategoryTypePageUrl = '/catalogue-category-type';
  let username: string;
  let password: string;
  const catalogueCategoryTypeSample = { name: 'nutritious' };

  let catalogueCategoryType;

  before(() => {
    cy.credentials().then(credentials => {
      ({ username, password } = credentials);
    });
  });

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/catalogue-category-types+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/catalogue-category-types').as('postEntityRequest');
    cy.intercept('DELETE', '/api/catalogue-category-types/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (catalogueCategoryType) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/catalogue-category-types/${catalogueCategoryType.id}`,
      }).then(() => {
        catalogueCategoryType = undefined;
      });
    }
  });

  it('CatalogueCategoryTypes menu should load CatalogueCategoryTypes page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('catalogue-category-type');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('CatalogueCategoryType').should('exist');
    cy.location('pathname').should('eq', catalogueCategoryTypePageUrl);
  });

  describe('CatalogueCategoryType page', () => {
    it('should have translated page title', () => {
      cy.visit(catalogueCategoryTypePageUrl);
      cy.getEntityHeading('CatalogueCategoryType').should('not.contain', 'snpApp.catalogueCategoryType.home.title');
    });

    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(catalogueCategoryTypePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create CatalogueCategoryType page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.location('pathname').should('eq', `${catalogueCategoryTypePageUrl}/new`);
        cy.getEntityCreateUpdateHeading('CatalogueCategoryType');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', catalogueCategoryTypePageUrl);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/catalogue-category-types',
          body: catalogueCategoryTypeSample,
        }).then(({ body }) => {
          catalogueCategoryType = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/catalogue-category-types+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [catalogueCategoryType],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(catalogueCategoryTypePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details CatalogueCategoryType page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('catalogueCategoryType');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', catalogueCategoryTypePageUrl);
      });

      it('edit button click should load edit CatalogueCategoryType page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('CatalogueCategoryType');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', catalogueCategoryTypePageUrl);
      });

      it('edit button click should load edit CatalogueCategoryType page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('CatalogueCategoryType');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', catalogueCategoryTypePageUrl);
      });

      it('last delete button click should delete instance of CatalogueCategoryType', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('catalogueCategoryType').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', catalogueCategoryTypePageUrl);

        catalogueCategoryType = undefined;
      });
    });
  });

  describe('new CatalogueCategoryType page', () => {
    beforeEach(() => {
      cy.visit(catalogueCategoryTypePageUrl);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('CatalogueCategoryType');
    });

    it('should create an instance of CatalogueCategoryType', () => {
      cy.get(`[data-cy="name"]`).type('geez peninsula gently');
      cy.get(`[data-cy="name"]`).should('have.value', 'geez peninsula gently');

      cy.get(`[data-cy="description"]`).type('perfumed eek old');
      cy.get(`[data-cy="description"]`).should('have.value', 'perfumed eek old');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        catalogueCategoryType = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.location('pathname').should('eq', catalogueCategoryTypePageUrl);
    });
  });
});
