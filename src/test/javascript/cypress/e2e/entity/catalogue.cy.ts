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

describe('Catalogue e2e test', () => {
  const cataloguePageUrl = '/catalogue';
  let username: string;
  let password: string;
  const catalogueSample = { name: 'writhing ack' };

  let catalogue;

  before(() => {
    cy.credentials().then(credentials => {
      ({ username, password } = credentials);
    });
  });

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/catalogues+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/catalogues').as('postEntityRequest');
    cy.intercept('DELETE', '/api/catalogues/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (catalogue) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/catalogues/${catalogue.id}`,
      }).then(() => {
        catalogue = undefined;
      });
    }
  });

  it('Catalogues menu should load Catalogues page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('catalogue');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Catalogue').should('exist');
    cy.location('pathname').should('eq', cataloguePageUrl);
  });

  describe('Catalogue page', () => {
    it('should have translated page title', () => {
      cy.visit(cataloguePageUrl);
      cy.getEntityHeading('Catalogue').should('not.contain', 'snpApp.catalogue.home.title');
    });

    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(cataloguePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Catalogue page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.location('pathname').should('eq', `${cataloguePageUrl}/new`);
        cy.getEntityCreateUpdateHeading('Catalogue');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', cataloguePageUrl);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/catalogues',
          body: catalogueSample,
        }).then(({ body }) => {
          catalogue = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/catalogues+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [catalogue],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(cataloguePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Catalogue page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('catalogue');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', cataloguePageUrl);
      });

      it('edit button click should load edit Catalogue page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Catalogue');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', cataloguePageUrl);
      });

      it('edit button click should load edit Catalogue page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Catalogue');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', cataloguePageUrl);
      });

      it('last delete button click should delete instance of Catalogue', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('catalogue').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', cataloguePageUrl);

        catalogue = undefined;
      });
    });
  });

  describe('new Catalogue page', () => {
    beforeEach(() => {
      cy.visit(cataloguePageUrl);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Catalogue');
    });

    it('should create an instance of Catalogue', () => {
      cy.get(`[data-cy="name"]`).type('aw nor vary');
      cy.get(`[data-cy="name"]`).should('have.value', 'aw nor vary');

      cy.get(`[data-cy="description"]`).type('representation gaseous appliance');
      cy.get(`[data-cy="description"]`).should('have.value', 'representation gaseous appliance');

      cy.get(`[data-cy="sequenceNo"]`).type('30687');
      cy.get(`[data-cy="sequenceNo"]`).should('have.value', '30687');

      cy.get(`[data-cy="imagePath"]`).type('condense once');
      cy.get(`[data-cy="imagePath"]`).should('have.value', 'condense once');

      cy.get(`[data-cy="mobileImagePath"]`).type('vanadyl solvency ugh');
      cy.get(`[data-cy="mobileImagePath"]`).should('have.value', 'vanadyl solvency ugh');

      cy.get(`[data-cy="altImage1"]`).type('utilization over accelerator');
      cy.get(`[data-cy="altImage1"]`).should('have.value', 'utilization over accelerator');

      cy.get(`[data-cy="altImage2"]`).type('service grade poorly');
      cy.get(`[data-cy="altImage2"]`).should('have.value', 'service grade poorly');

      cy.get(`[data-cy="altImage3"]`).type('respectful programme');
      cy.get(`[data-cy="altImage3"]`).should('have.value', 'respectful programme');

      cy.get(`[data-cy="bannerUrl"]`).type('wherever superficial');
      cy.get(`[data-cy="bannerUrl"]`).should('have.value', 'wherever superficial');

      cy.get(`[data-cy="isBanner"]`).should('not.be.checked');
      cy.get(`[data-cy="isBanner"]`).click();
      cy.get(`[data-cy="isBanner"]`).should('be.checked');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        catalogue = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.location('pathname').should('eq', cataloguePageUrl);
    });
  });
});
